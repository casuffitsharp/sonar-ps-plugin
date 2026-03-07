package org.sonar.plugins.powershell.fillers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.powershell.ast.Tokens;
import org.sonar.plugins.powershell.ast.Tokens.Token;
import org.sonar.plugins.powershell.metrics.PowershellMetrics;
import org.sonar.plugins.powershell.utils.ContextWriteGuard;

public class HalsteadComplexityFiller implements IFiller {

  private static final Logger LOGGER = LoggerFactory.getLogger(HalsteadComplexityFiller.class);

  private static final List<String> skipTypes = Arrays.asList("EndOfInput", "NewLine", "Comment");

  private static final List<String> operandTypes =
      Arrays.asList(
          "StringExpandable",
          "Variable",
          "SplattedVariable",
          "StringLiteral",
          "HereStringExpandable",
          "HereStringLiteral");

  @Override
  public void fill(
      final SensorContext context,
      final InputFile f,
      final Tokens tokens,
      ContextWriteGuard writeGuard) {
    try {
      int totalOperands = 0;
      final List<String> uniqueOperators = new LinkedList<>();
      final List<String> uniqueOperands = new LinkedList<>();

      for (final Token token : tokens.getTokens()) {
        if (!skipTypes.contains(token.kind()) && token.text() != null) {
          final String text = token.text().toLowerCase();
          if (operandTypes.contains(token.kind())) {
            totalOperands++;
            if (!uniqueOperands.contains(text)) {
              uniqueOperands.add(text);
            }
          } else if (!uniqueOperators.contains(text)) {
            uniqueOperators.add(text);
          }
        }
      }

      int difficulty;
      if (uniqueOperands.isEmpty()) {
        difficulty = 0;
      } else {
        difficulty =
            (int)
                ((int) Math.ceil(uniqueOperators.size() / 2.0)
                    * ((totalOperands * 1.0) / uniqueOperands.size()));
      }

      writeGuard.write(
          () ->
              context
                  .<Integer>newMeasure()
                  .on(f)
                  .forMetric(PowershellMetrics.HALSTEAD_DIFFICULTY)
                  .withValue(difficulty)
                  .save());

    } catch (final Exception e) {
      LOGGER.warn("Exception while saving halstead difficulty metric", e);
    }
  }
}
