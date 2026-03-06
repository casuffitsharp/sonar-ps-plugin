package org.sonar.plugins.powershell.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PowershellPluginExceptionTest {

  @Test
  public void shouldStoreMessage() {
    PowershellPluginException exception = new PowershellPluginException("test message");
    assertEquals("test message", exception.getMessage());
  }

  @Test
  public void shouldStoreMessageAndCause() {
    Throwable cause = new RuntimeException("cause");
    PowershellPluginException exception = new PowershellPluginException("test message", cause);
    assertEquals("test message", exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}
