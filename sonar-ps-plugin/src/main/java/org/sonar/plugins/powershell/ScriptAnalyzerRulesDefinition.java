package org.sonar.plugins.powershell;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.issue.impact.SoftwareQuality;
import org.sonar.api.rules.CleanCodeAttribute;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ScriptAnalyzerRulesDefinition implements RulesDefinition {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScriptAnalyzerRulesDefinition.class);

  private static final Map<String, org.sonar.api.issue.impact.Severity> SEVERITY_MAPPING =
      new HashMap<>();

  static {
    SEVERITY_MAPPING.put("INFO", org.sonar.api.issue.impact.Severity.LOW);
    SEVERITY_MAPPING.put("MINOR", org.sonar.api.issue.impact.Severity.LOW);
    SEVERITY_MAPPING.put("MAJOR", org.sonar.api.issue.impact.Severity.MEDIUM);
    SEVERITY_MAPPING.put("CRITICAL", org.sonar.api.issue.impact.Severity.HIGH);
    SEVERITY_MAPPING.put("BLOCKER", org.sonar.api.issue.impact.Severity.BLOCKER);
  }

  public void define(final Context context) {
    final String repositoryKey = Constants.REPO_KEY;
    final String repositoryName = Constants.REPO_NAME;
    final String languageKey = PowershellLanguage.KEY;
    defineRulesForLanguage(context, repositoryKey, repositoryName, languageKey);
  }

  private void defineRulesForLanguage(
      final Context context,
      final String repositoryKey,
      final String repositoryName,
      String languageKey) {
    final NewRepository repository =
        context.createRepository(repositoryKey, languageKey).setName(repositoryName);

    try (final InputStream rulesXml =
        this.getClass().getClassLoader().getResourceAsStream(Constants.RULES_DEFINITION_FILE)) {

      if (rulesXml == null) {
        LOGGER.error("Rules definition file not found: {}", Constants.RULES_DEFINITION_FILE);
        return;
      }

      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      final DocumentBuilder builder = factory.newDocumentBuilder();
      final Document xmlDoc = builder.parse(rulesXml);

      final NodeList nodes = xmlDoc.getElementsByTagName("rule");
      for (int i = 0; i < nodes.getLength(); i++) {
        registerRule(repository, nodes.item(i), i);
      }
    } catch (final Exception e1) {
      LOGGER.warn("Unexpected error while registering rules", e1);
    } finally {
      repository.done();
    }
  }

  private void registerRule(final NewRepository repository, final Node node, int index) {
    try {
      final String key = getChild(node, "key");
      final String name = getChild(node, "name");
      final String description = getChild(node, "description");

      final String constantPerIssue = getChild(node, "debtRemediationFunctionCoefficient");
      final String severity = getChild(node, "severity");
      final String type = getChild(node, "type");
      final String cleanCodeAttribute = getChild(node, "cleanCodeAttribute");
      final String softwareQualityImpact = getChild(node, "softwareQualityImpact");

      final NewRule rule =
          repository
              .createRule(key)
              .setName(name)
              .setHtmlDescription(description)
              .setSeverity(severity);

      if (type != null && !type.trim().isEmpty()) {
        try {
          rule.setType(RuleType.valueOf(type.trim()));
        } catch (IllegalArgumentException e) {
          LOGGER.warn("Unknown rule type: {}", type);
        }
      }

      if (cleanCodeAttribute != null && !cleanCodeAttribute.trim().isEmpty()) {
        try {
          rule.setCleanCodeAttribute(CleanCodeAttribute.valueOf(cleanCodeAttribute.trim()));
        } catch (IllegalArgumentException e) {
          LOGGER.warn("Unknown clean code attribute: {}", cleanCodeAttribute);
        }
      }

      if (softwareQualityImpact != null && !softwareQualityImpact.trim().isEmpty()) {
        try {
          SoftwareQuality quality = SoftwareQuality.valueOf(softwareQualityImpact.trim());
          org.sonar.api.issue.impact.Severity impactSeverity =
              SEVERITY_MAPPING.getOrDefault(
                  severity.trim(), org.sonar.api.issue.impact.Severity.MEDIUM);
          rule.addDefaultImpact(quality, impactSeverity);
        } catch (IllegalArgumentException e) {
          LOGGER.warn("Unknown software quality: {}", softwareQualityImpact);
        }
      }

      rule.setDebtRemediationFunction(
          rule.debtRemediationFunctions().constantPerIssue(constantPerIssue));
    } catch (final Exception e) {
      LOGGER.warn("Unexpected error while registering rule: {}", index, e);
    }
  }

  private static String getChild(final Node parent, final String key) {
    final NodeList nodes = parent.getChildNodes();
    final int cnt = nodes.getLength();
    for (int i = 0; i < cnt; i++) {
      Node node = nodes.item(i);
      if (key.equalsIgnoreCase(node.getNodeName())) {
        return node.getTextContent();
      }
    }
    return "";
  }
}
