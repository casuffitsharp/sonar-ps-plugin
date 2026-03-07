package org.sonar.plugins.powershell.fillers;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.plugins.powershell.ast.Tokens;
import org.sonar.plugins.powershell.ast.Tokens.Token;
import org.sonar.plugins.powershell.utils.ContextWriteGuard;

public class HighlightingFiller implements IFiller {

  private static final Logger LOGGER = LoggerFactory.getLogger(HighlightingFiller.class);

  @Override
  public void fill(
      final SensorContext context,
      final InputFile f,
      final Tokens tokens,
      ContextWriteGuard writeGuard) {
    try {
      final NewHighlighting highlighting = context.newHighlighting().onFile(f);
      for (final Token token : tokens.getTokens()) {
        highlightToken(highlighting, token);
      }
      writeGuard.write(highlighting::save);
    } catch (Exception e) {
      LOGGER.warn("Exception while running highlighting", e);
    }
  }

  private static void highlightToken(final NewHighlighting highlighting, final Token token) {
    try {
      final List<String> kinds = Arrays.asList(token.tokenFlags().toLowerCase().split(","));
      int startLine = token.startLineNumber();
      int startLineOffset = token.startColumnNumber() - 1;
      int endLine = token.endLineNumber();
      int endLineOffset = token.endColumnNumber() - 1;
      if (check("comment", token, kinds)) {
        highlighting.highlight(
            startLine, startLineOffset, endLine, endLineOffset, TypeOfText.COMMENT);
        return;
      }
      if (check("keyword", token, kinds)) {
        highlighting.highlight(
            startLine, startLineOffset, endLine, endLineOffset, TypeOfText.KEYWORD);
        return;
      }
      if (check("StringLiteral", token, kinds) || check("StringExpandable", token, kinds)) {
        highlighting.highlight(
            startLine, startLineOffset, endLine, endLineOffset, TypeOfText.STRING);
        return;
      }
      if (check("Variable", token, kinds)) {
        highlighting.highlight(
            startLine, startLineOffset, endLine, endLineOffset, TypeOfText.KEYWORD_LIGHT);
      }
    } catch (Exception e) {
      LOGGER.warn("Exception while adding highlighting for: {}", token, e);
    }
  }

  private static boolean check(final String txt, final Token token, final List<String> kinds) {
    return txt.equalsIgnoreCase(token.kind()) || kinds.contains(txt.toLowerCase());
  }
}
