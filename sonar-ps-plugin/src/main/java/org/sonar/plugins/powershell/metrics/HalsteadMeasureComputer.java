package org.sonar.plugins.powershell.metrics;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

public class HalsteadMeasureComputer implements MeasureComputer {

  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext defContext) {
    return defContext
        .newDefinitionBuilder()
        .setInputMetrics(
            PowershellMetrics.HALSTEAD_DIFFICULTY.getKey(),
            PowershellMetrics.HALSTEAD_VOLUME.getKey(),
            PowershellMetrics.HALSTEAD_EFFORT.getKey())
        .setOutputMetrics(
            PowershellMetrics.HALSTEAD_DIFFICULTY.getKey(),
            PowershellMetrics.HALSTEAD_VOLUME.getKey(),
            PowershellMetrics.HALSTEAD_EFFORT.getKey())
        .build();
  }

  @Override
  public void compute(MeasureComputerContext context) {
    if (context.getComponent().getType() != Component.Type.FILE) {
      int sumDifficulty = 0;
      double sumVolume = 0.0;
      double sumEffort = 0.0;

      for (Measure measure :
          context.getChildrenMeasures(PowershellMetrics.HALSTEAD_DIFFICULTY.getKey())) {
        sumDifficulty += measure.getIntValue();
      }
      for (Measure measure :
          context.getChildrenMeasures(PowershellMetrics.HALSTEAD_VOLUME.getKey())) {
        sumVolume += measure.getDoubleValue();
      }
      for (Measure measure :
          context.getChildrenMeasures(PowershellMetrics.HALSTEAD_EFFORT.getKey())) {
        sumEffort += measure.getDoubleValue();
      }

      context.addMeasure(PowershellMetrics.HALSTEAD_DIFFICULTY.getKey(), sumDifficulty);
      context.addMeasure(PowershellMetrics.HALSTEAD_VOLUME.getKey(), sumVolume);
      context.addMeasure(PowershellMetrics.HALSTEAD_EFFORT.getKey(), sumEffort);
    }
  }
}
