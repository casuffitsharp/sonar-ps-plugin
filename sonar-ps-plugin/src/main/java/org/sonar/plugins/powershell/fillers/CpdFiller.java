package org.sonar.plugins.powershell.fillers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.plugins.powershell.ast.Tokens;
import org.sonar.plugins.powershell.ast.Tokens.Token;
import org.sonar.plugins.powershell.utils.ContextWriteGuard;

public class CpdFiller implements IFiller {

  private static final Logger LOGGER = LoggerFactory.getLogger(CpdFiller.class);

  @Override
  public void fill(
      final SensorContext context,
      final InputFile f,
      final Tokens tokens,
      ContextWriteGuard writeGuard) {
    try {
      final NewCpdTokens cpdTokens = context.newCpdTokens().onFile(f);

      for (final Token token : tokens.getTokens()) {
        if (StringUtils.isBlank(token.text())) {
          continue;
        }
        tryAddToken(cpdTokens, token);
      }
      writeGuard.write(cpdTokens::save);
    } catch (final Exception e) {
      LOGGER.warn("Exception while saving tokens", e);
    }
  }

  private static void tryAddToken(final NewCpdTokens cpdTokens, final Token token) {
    try {
      cpdTokens.addToken(
          token.startLineNumber(),
          token.startColumnNumber() - 1,
          token.endLineNumber(),
          token.endColumnNumber() - 1,
          token.text());
    } catch (final Exception e) {
      LOGGER.debug("Exception while adding token: {}", token, e);
    }
  }
}
