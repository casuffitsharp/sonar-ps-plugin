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

  public String[] getFileSuffixes(String[] defaultSuffixes) {
    return config
        .get(Constants.FILE_SUFFIXES)
        .map(
            s ->
                java.util.Arrays.stream(s.split(","))
                    .map(String::trim)
                    .map(suffix -> suffix.startsWith(".") ? suffix.substring(1) : suffix)
                    .filter(suffix -> !suffix.isEmpty())
                    .toArray(String[]::new))
        .orElse(defaultSuffixes);
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
}
