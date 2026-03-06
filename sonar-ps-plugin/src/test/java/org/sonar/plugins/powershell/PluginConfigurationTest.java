package org.sonar.plugins.powershell;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Configuration;

public class PluginConfigurationTest {

  private Configuration configuration;
  private PluginConfiguration sut;

  @Before
  public void setUp() {
    configuration = mock(Configuration.class);
    sut = new PluginConfiguration(configuration);
  }

  @Test
  public void shouldReturnIsPluginSkipped() {
    when(configuration.getBoolean(Constants.SKIP_PLUGIN)).thenReturn(Optional.of(true));
    assertTrue(sut.isPluginSkipped());

    when(configuration.getBoolean(Constants.SKIP_PLUGIN)).thenReturn(Optional.of(false));
    assertFalse(sut.isPluginSkipped());

    when(configuration.getBoolean(Constants.SKIP_PLUGIN)).thenReturn(Optional.empty());
    assertFalse(sut.isPluginSkipped());
  }

  @Test
  public void shouldReturnIsTokenizerSkipped() {
    when(configuration.getBoolean(Constants.SKIP_TOKENIZER)).thenReturn(Optional.of(true));
    assertTrue(sut.isTokenizerSkipped());

    when(configuration.getBoolean(Constants.SKIP_TOKENIZER)).thenReturn(Optional.empty());
    assertFalse(sut.isTokenizerSkipped());
  }

  @Test
  public void shouldReturnTokenizerTimeout() {
    when(configuration.get(Constants.TIMEOUT_TOKENIZER)).thenReturn(Optional.of("120"));
    assertEquals(120L, sut.getTokenizerTimeout());

    when(configuration.get(Constants.TIMEOUT_TOKENIZER)).thenReturn(Optional.empty());
    assertEquals(3600L, sut.getTokenizerTimeout());
  }

  @Test
  public void shouldReturnFileSuffixes() {
    String[] defaults = new String[] {"ps1"};
    when(configuration.get(Constants.FILE_SUFFIXES)).thenReturn(Optional.of("ps1,psm1,psd1"));
    assertArrayEquals(new String[] {"ps1", "psm1", "psd1"}, sut.getFileSuffixes(defaults));

    when(configuration.get(Constants.FILE_SUFFIXES)).thenReturn(Optional.empty());
    assertArrayEquals(defaults, sut.getFileSuffixes(defaults));
  }

  @Test
  public void shouldReturnPowershellExecutable() {
    when(configuration.get(Constants.PS_EXECUTABLE)).thenReturn(Optional.of(" pwsh.exe "));
    assertEquals("pwsh.exe", sut.getPowershellExecutable());

    when(configuration.get(Constants.PS_EXECUTABLE)).thenReturn(Optional.of(""));
    assertNull(sut.getPowershellExecutable());

    when(configuration.get(Constants.PS_EXECUTABLE)).thenReturn(Optional.empty());
    assertNull(sut.getPowershellExecutable());
  }

  @Test
  public void shouldNormalizeFileSuffixes() {
    String[] defaults = new String[] {"ps1"};
    when(configuration.get(Constants.FILE_SUFFIXES))
        .thenReturn(Optional.of(" .ps1 , psm1 ,.psd1 "));
    assertArrayEquals(new String[] {"ps1", "psm1", "psd1"}, sut.getFileSuffixes(defaults));
  }

  @Test
  public void shouldReturnExternalRulesSkipList() {
    String[] skipList = new String[] {"Rule1", "Rule2"};
    when(configuration.getStringArray(Constants.EXTERNAL_RULES_SKIP_LIST)).thenReturn(skipList);
    assertArrayEquals(skipList, sut.getExternalRulesSkipList());
  }
}
