package org.sonar.plugins.powershell.metrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PowershellMetricsTest {

  @Test
  public void testGetMetrics() {
    PowershellMetrics metrics = new PowershellMetrics();
    assertEquals(3, metrics.getMetrics().size());
    assertTrue(metrics.getMetrics().contains(PowershellMetrics.HALSTEAD_DIFFICULTY));
    assertTrue(metrics.getMetrics().contains(PowershellMetrics.HALSTEAD_VOLUME));
    assertTrue(metrics.getMetrics().contains(PowershellMetrics.HALSTEAD_EFFORT));
  }
}
