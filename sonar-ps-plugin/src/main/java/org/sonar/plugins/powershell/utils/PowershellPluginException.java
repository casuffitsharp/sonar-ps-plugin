package org.sonar.plugins.powershell.utils;

public class PowershellPluginException extends RuntimeException {

  public PowershellPluginException(String message) {
    super(message);
  }

  public PowershellPluginException(String message, Throwable cause) {
    super(message, cause);
  }
}
