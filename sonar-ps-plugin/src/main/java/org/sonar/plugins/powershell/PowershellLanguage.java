package org.sonar.plugins.powershell;

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

/** Defines the PowerShell language for SonarQube. */
@SuppressWarnings("java:S2160") // Subclasses should override "equals"
public class PowershellLanguage extends AbstractLanguage {

  public static final String KEY = "ps";
  public static final String NAME = "Powershell";
  protected static final String[] DEFAULT_FILE_SUFFIXES = new String[] {"ps1", "psm1", "psd1"};

  private final PluginConfiguration powershellConfig;

  public PowershellLanguage(Configuration config) {
    super(KEY, NAME);
    this.powershellConfig = new PluginConfiguration(config);
  }

  @Override
  public String[] getFileSuffixes() {
    return powershellConfig.getFileSuffixes(DEFAULT_FILE_SUFFIXES);
  }
}
