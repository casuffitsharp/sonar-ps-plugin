package org.sonar.plugins.powershell.fillers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.plugins.powershell.ast.Tokens;
import org.sonar.plugins.powershell.utils.ContextWriteGuard;

public class CognitiveComplexityFiller implements IFiller {

  private static final Logger LOGGER = LoggerFactory.getLogger(CognitiveComplexityFiller.class);

  @Override
  public void fill(
      final SensorContext context,
      final InputFile f,
      final Tokens tokens,
      ContextWriteGuard writeGuard) {
    try {
      writeGuard.write(
          () ->
              context
                  .<Integer>newMeasure()
                  .on(f)
                  .forMetric(CoreMetrics.COGNITIVE_COMPLEXITY)
                  .withValue(tokens.getCognitiveComplexity())
                  .save());
    } catch (final Exception e) {
      LOGGER.warn("Exception while saving cognitive complexity", e);
    }
  }
}
