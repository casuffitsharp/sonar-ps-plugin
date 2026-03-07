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
      int totalOperators = 0;
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
          } else {
            totalOperators++;
            if (!uniqueOperators.contains(text)) {
              uniqueOperators.add(text);
            }
          }
        }
      }

      int difficulty = 0;
      int volume = 0;
      int effort = 0;

      int n1 = uniqueOperators.size();
      int n2 = uniqueOperands.size();
      int bigN1 = totalOperators;
      int bigN2 = totalOperands;

      if (n2 > 0) {
        difficulty = (int) Math.ceil((n1 / 2.0) * ((bigN2 * 1.0) / n2));
      }

      int n = n1 + n2;
      int bigN = bigN1 + bigN2;

      if (n > 0) {
        volume = (int) Math.round(bigN * (Math.log(n) / Math.log(2)));
      }

      effort = difficulty * volume;

      int finalDifficulty = difficulty;
      writeGuard.write(
          () ->
              context
                  .<Integer>newMeasure()
                  .on(f)
                  .forMetric(PowershellMetrics.HALSTEAD_DIFFICULTY)
                  .withValue(finalDifficulty)
                  .save());

      int finalVolume = volume;
      writeGuard.write(
          () ->
              context
                  .<Double>newMeasure()
                  .on(f)
                  .forMetric(PowershellMetrics.HALSTEAD_VOLUME)
                  .withValue((double) finalVolume)
                  .save());

      int finalEffort = effort;
      writeGuard.write(
          () ->
              context
                  .<Double>newMeasure()
                  .on(f)
                  .forMetric(PowershellMetrics.HALSTEAD_EFFORT)
                  .withValue((double) finalEffort)
                  .save());

    } catch (final Exception e) {
      LOGGER.warn("Exception while saving halstead difficulty metric", e);
    }
  }
}
