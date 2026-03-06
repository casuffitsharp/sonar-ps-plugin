package org.sonar.plugins.powershell.fillers;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.plugins.powershell.ast.Tokens;
import org.sonar.plugins.powershell.ast.Tokens.Token;

public class LineMeasuresFiller implements IFiller {

  private static final Logger LOGGER = LoggerFactory.getLogger(LineMeasuresFiller.class);

  private static final List<String> skipTypes = Arrays.asList("EndOfInput", "NewLine");

  private static final int COMMENT = 1;
  private static final int CODE = 2;

  @Override
  public void fill(
      final SensorContext context,
      final InputFile f,
      final Tokens tokens,
      ContextWriteGuard writeGuard) {
    try {
      final long[] lines = new long[f.lines() + 1];
      markLines(lines, tokens.getTokens());

      int commentLineCount = 0;
      int nonCommentLineCount = 0;

      for (int i = 0; i < lines.length; i++) {
        if (lines[i] == COMMENT) {
          commentLineCount++;
        } else if ((lines[i] & CODE) == CODE) {
          nonCommentLineCount++;
        }
      }

      final int finalCommentLineCount = commentLineCount;
      final int finalNonCommentLineCount = nonCommentLineCount;
      writeGuard.write(
          () -> {
            context
                .<Integer>newMeasure()
                .on(f)
                .forMetric(CoreMetrics.COMMENT_LINES)
                .withValue(finalCommentLineCount)
                .save();
            context
                .<Integer>newMeasure()
                .on(f)
                .forMetric(CoreMetrics.NCLOC)
                .withValue(finalNonCommentLineCount)
                .save();
          });
    } catch (final Exception e) {
      LOGGER.warn("Exception while calculating comment lines ", e);
    }
  }

  private void markLines(long[] lines, List<Token> tokens) {
    for (final Token token : tokens) {
      if (!skipTypes.contains(token.kind()) && token.text() != null) {
        int type = "Comment".equalsIgnoreCase(token.kind()) ? COMMENT : CODE;
        for (int i = token.startLineNumber(); i <= token.endLineNumber(); i++) {
          if (i < lines.length) {
            lines[i] |= type;
          }
        }
      }
    }
  }
}
