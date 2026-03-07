package org.sonar.plugins.powershell.metrics;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PowershellMetricsTest {

  @Test
  public void testGetMetrics() {
    PowershellMetrics metrics = new PowershellMetrics();
    assertTrue(metrics.getMetrics().size() >= 3);
    assertTrue(metrics.getMetrics().contains(PowershellMetrics.HALSTEAD_DIFFICULTY));
    assertTrue(metrics.getMetrics().contains(PowershellMetrics.HALSTEAD_VOLUME));
    assertTrue(metrics.getMetrics().contains(PowershellMetrics.HALSTEAD_EFFORT));
  }
}
