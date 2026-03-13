package org.sonar.plugins.powershell;

import org.sonar.api.config.Configuration;

/** Wrapper for SonarQube Configuration to provide typed access to PowerShell plugin settings. */
public class PluginConfiguration {

  private final Configuration config;

  public PluginConfiguration(Configuration config) {
    this.config = config;
  }

  public boolean isPluginSkipped() {
    return config.getBoolean(Constants.SKIP_PLUGIN).orElse(false);
  }

  public boolean isTokenizerSkipped() {
    return config.getBoolean(Constants.SKIP_TOKENIZER).orElse(false);
  }

  public long getTokenizerTimeout() {
    return config
        .get(Constants.TIMEOUT_TOKENIZER)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .map(
            value -> {
              try {
                return Long.parseLong(value);
              } catch (NumberFormatException e) {
                return 3600L;
              }
            })
        .orElse(3600L);
  }

  public String[] getFileSuffixes() {
    return getFileSuffixes(PowershellLanguage.DEFAULT_FILE_SUFFIXES);
  }

  public String[] getFileSuffixes(String[] defaultSuffixes) {
    String[] suffixes = config.getStringArray(Constants.FILE_SUFFIXES);
    if (suffixes == null || suffixes.length == 0) {
      return defaultSuffixes;
    }
    return java.util.Arrays.stream(suffixes)
        .map(String::trim)
        .map(suffix -> suffix.startsWith(".") ? suffix.substring(1) : suffix)
        .filter(suffix -> !suffix.isEmpty())
        .toArray(String[]::new);
  }

  public String getPowershellExecutable() {
    return config
        .get(Constants.PS_EXECUTABLE)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .orElse(null);
  }

  public String[] getExternalRulesSkipList() {
    return config.getStringArray(Constants.EXTERNAL_RULES_SKIP_LIST);
  }

  public boolean isPsAnalyzerAutoInstall() {
    return config.getBoolean(Constants.PS_ANALYZER_AUTO_INSTALL).orElse(true);
  }
}
