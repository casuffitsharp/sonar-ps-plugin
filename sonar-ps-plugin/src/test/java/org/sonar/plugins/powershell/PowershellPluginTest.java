package org.sonar.plugins.powershell;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.sonar.api.Plugin;

public class PowershellPluginTest {

  @Test
  public void testDefine() {
    PowershellPlugin plugin = new PowershellPlugin();
    Plugin.Context context = mock(Plugin.Context.class);

    plugin.define(context);

    // Verify that some extensions were added
    verify(context, atLeastOnce()).addExtension(any());
    verify(context, atLeastOnce()).addExtensions(any(), any(), any(), any());
  }
}
