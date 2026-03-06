package org.sonar.plugins.powershell;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PowershellQualityProfile implements BuiltInQualityProfilesDefinition {

  private static final Logger LOGGER = LoggerFactory.getLogger(PowershellQualityProfile.class);

  @Override
  public void define(final Context context) {
    final NewBuiltInQualityProfile profile =
        context
            .createBuiltInQualityProfile(Constants.PROFILE_NAME, PowershellLanguage.KEY)
            .setDefault(true);
    final String repositoryKey = Constants.REPO_KEY;

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

      final NodeList nodes = xmlDoc.getElementsByTagName("key");
      for (int i = 0; i < nodes.getLength(); i++) {
        final Node node = nodes.item(i);
        final String key = node.getTextContent();
        profile.activateRule(repositoryKey, key);
      }

    } catch (ParserConfigurationException | org.xml.sax.SAXException | java.io.IOException e) {
      LOGGER.warn("Unexpected error while registering rules", e);
    } finally {
      profile.done();
    }
  }
}
