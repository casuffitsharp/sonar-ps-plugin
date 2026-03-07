package org.sonar.plugins.powershell.metrics;

import java.util.Arrays;
import java.util.List;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

public class PowershellMetrics implements Metrics {

  public static final Metric<Integer> HALSTEAD_DIFFICULTY =
      new Metric.Builder("halstead_difficulty", "Halstead Difficulty", Metric.ValueType.INT)
          .setDescription("Halstead Difficulty (D)")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
          .create();

  public static final Metric<Double> HALSTEAD_VOLUME =
      new Metric.Builder("halstead_volume", "Halstead Volume", Metric.ValueType.FLOAT)
          .setDescription("Halstead Volume (V)")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
          .create();

  public static final Metric<Double> HALSTEAD_EFFORT =
      new Metric.Builder("halstead_effort", "Halstead Effort", Metric.ValueType.FLOAT)
          .setDescription("Halstead Effort (E)")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
          .create();

  @Override
  public List<Metric> getMetrics() {
    return Arrays.asList(HALSTEAD_DIFFICULTY, HALSTEAD_VOLUME, HALSTEAD_EFFORT);
  }
}
