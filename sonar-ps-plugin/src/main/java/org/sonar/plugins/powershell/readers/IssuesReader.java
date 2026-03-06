package org.sonar.plugins.powershell.readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.plugins.powershell.issues.PsIssue;
import org.sonar.plugins.powershell.utils.PowershellPluginException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class IssuesReader {

  private static final Logger LOGGER = LoggerFactory.getLogger(IssuesReader.class);

  public List<PsIssue> read(final File file)
      throws ParserConfigurationException, SAXException, IOException {
    try (final InputStream main = new FileInputStream(file)) {
      return read(main);
    }
  }

  public List<PsIssue> read(final InputStream main)
      throws ParserConfigurationException, SAXException, IOException {
    final List<PsIssue> issues = new LinkedList<>();
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    final DocumentBuilder builder = factory.newDocumentBuilder();

    try (final BOMInputStream bomInputStream =
        BOMInputStream.builder().setInputStream(main).get()) {
      final Document doc = builder.parse(bomInputStream);
      final NodeList list = doc.getElementsByTagName("Object");
      for (int i = 0; i < list.getLength(); i++) {
        final Node node = list.item(i);
        try {
          String ruleId = getNodeByAttributeName(node, "RuleName").getTextContent();
          String message = getNodeByAttributeName(node, "Message").getTextContent();
          String lineStr = getNodeByAttributeName(node, "Line").getTextContent();
          int line = 0;
          if (!StringUtils.isEmpty(lineStr)) {
            line = Integer.parseInt(lineStr);
          }
          String severity = getNodeByAttributeName(node, "Severity").getTextContent();
          String fileAttr = getNodeByAttributeName(node, "File").getTextContent();

          issues.add(new PsIssue(ruleId, message, line, severity, fileAttr));
        } catch (Exception e) {
          LOGGER.warn("Unexpected error reading results from: {}", node.getTextContent(), e);
        }
      }
    }
    return issues;
  }

  protected static final Node getNodeByAttributeName(final Node root, final String name) {
    final NodeList children = root.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      final Node child = children.item(i);
      if (child.getAttributes() == null) {
        continue;
      }
      final Node attribute = child.getAttributes().getNamedItem("Name");
      if (attribute != null && attribute.getTextContent().equalsIgnoreCase(name)) {
        return child;
      }
    }
    throw new PowershellPluginException(
        "Child node with attribute named: '" + name + "' was not found");
  }
}
