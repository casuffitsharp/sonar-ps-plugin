package org.sonar.plugins.powershell.readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.plugins.powershell.ast.Tokens;
import org.sonar.plugins.powershell.utils.PowershellPluginException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TokensReader {

  private static final Logger LOGGER = LoggerFactory.getLogger(TokensReader.class);

  public Tokens read(final File file)
      throws ParserConfigurationException, SAXException, IOException {
    final Tokens tokens = new Tokens();
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    final DocumentBuilder builder = factory.newDocumentBuilder();
    final Document doc;
    try (final FileInputStream fileinputstream = new FileInputStream(file)) {
      try (final BOMInputStream bomInputStream =
          BOMInputStream.builder().setInputStream(fileinputstream).get()) {
        doc = builder.parse(bomInputStream);
      }
    }
    tokens.setComplexity(Integer.parseInt(doc.getDocumentElement().getAttribute("complexity")));
    final NodeList list = doc.getElementsByTagName("Token");
    for (int i = 0; i < list.getLength(); i++) {
      try {
        final Node node = list.item(i);
        final String text = getChildByName(node, "Text").getTextContent();
        final String value = getChildByName(node, "Value").getTextContent();
        final String tokenFlags = getChildByName(node, "TokenFlags").getTextContent();
        final String kind = getChildByName(node, "Kind").getTextContent();
        final String cType = getChildByName(node, "CType").getTextContent();

        final int startLineNumber =
            Integer.parseInt(getChildByName(node, "StartLineNumber").getTextContent());
        final int endLineNumber =
            Integer.parseInt(getChildByName(node, "EndLineNumber").getTextContent());
        final long startOffset =
            Long.parseLong(getChildByName(node, "StartOffset").getTextContent());
        final long endOffset = Long.parseLong(getChildByName(node, "EndOffset").getTextContent());
        final int startColumnNumber =
            Integer.parseInt(getChildByName(node, "StartColumnNumber").getTextContent());
        final int endColumnNumber =
            Integer.parseInt(getChildByName(node, "EndColumnNumber").getTextContent());

        final Tokens.Token token =
            new Tokens.Token(
                text,
                value,
                tokenFlags,
                kind,
                cType,
                startLineNumber,
                endLineNumber,
                startColumnNumber,
                endColumnNumber,
                startOffset,
                endOffset);
        tokens.getTokens().add(token);
      } catch (Exception e) {
        LOGGER.warn("Unexpected error reading results", e);
      }
    }
    return tokens;
  }

  protected static final Node getChildByName(final Node root, final String name) {
    final NodeList children = root.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      final Node child = children.item(i);
      if (name.equalsIgnoreCase(child.getNodeName())) {
        return child;
      }
    }
    throw new PowershellPluginException("Child node with name " + name + " was not found");
  }
}
