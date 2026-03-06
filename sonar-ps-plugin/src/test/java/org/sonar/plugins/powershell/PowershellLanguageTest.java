package org.sonar.plugins.powershell;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.config.internal.MapSettings;

public class PowershellLanguageTest {

  @Test
  public void testGetFileSuffixes() {
    MapSettings settings = new MapSettings();
    settings.setProperty(Constants.FILE_SUFFIXES, "ps1,psm1");
    PowershellLanguage language = new PowershellLanguage(settings.asConfig());

    Assert.assertEquals(2, language.getFileSuffixes().length);
    Assert.assertEquals("ps1", language.getFileSuffixes()[0]);
    Assert.assertEquals("psm1", language.getFileSuffixes()[1]);
  }

  @Test
  public void testDefaultFileSuffixes() {
    MapSettings settings = new MapSettings();
    PowershellLanguage language = new PowershellLanguage(settings.asConfig());

    Assert.assertEquals(3, language.getFileSuffixes().length);
    Assert.assertEquals("ps1", language.getFileSuffixes()[0]);
  }
}
