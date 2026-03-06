package org.sonar.plugins.powershell.sensors;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.TempFolder;
import org.sonar.plugins.powershell.PluginConfiguration;
import org.sonar.plugins.powershell.PowershellLanguage;
import org.sonar.plugins.powershell.ast.Tokens;
import org.sonar.plugins.powershell.fillers.CComplexityFiller;
import org.sonar.plugins.powershell.fillers.ContextWriteGuard;
import org.sonar.plugins.powershell.fillers.CpdFiller;
import org.sonar.plugins.powershell.fillers.HalsteadComplexityFiller;
import org.sonar.plugins.powershell.fillers.HighlightingFiller;
import org.sonar.plugins.powershell.fillers.IFiller;
import org.sonar.plugins.powershell.fillers.LineMeasuresFiller;
import org.sonar.plugins.powershell.readers.TokensReader;
import org.sonar.plugins.powershell.utils.PowershellScriptExecutor;

public class TokenizerSensor extends BaseSensor {

  private static final Logger LOGGER = LoggerFactory.getLogger(TokenizerSensor.class);

  private final IFiller[] fillers =
      new IFiller[] {
        new LineMeasuresFiller(),
        new CpdFiller(),
        new HighlightingFiller(),
        new HalsteadComplexityFiller(),
        new CComplexityFiller()
      };
  private final TokensReader reader = new TokensReader();
  private final TempFolder folder;

  public TokenizerSensor(final TempFolder folder, PluginConfiguration config) {
    super(config);
    this.folder = folder;
  }

  @Override
  protected void innerExecute(final SensorContext context) {
    long timeoutSeconds = config.getTokenizerTimeout();

    if (config.isTokenizerSkipped()) {
      LOGGER.debug("Skipping tokenizer as skip flag is set");
      return;
    }

    final org.sonar.api.batch.fs.FileSystem fs = context.fileSystem();
    final FilePredicates p = fs.predicates();
    final ContextWriteGuard writeGuard = new ContextWriteGuard();

    final File scriptFile;
    try {
      scriptFile = prepareScript(folder, "/parser.ps1", "parser.ps1");
    } catch (IOException e) {
      LOGGER.error("Failed to prepare tokenizer script", e);
      return;
    }

    ExecutorService service = Executors.newWorkStealingPool();
    try {
      final Iterable<InputFile> inputFiles =
          fs.inputFiles(p.and(p.hasLanguage(PowershellLanguage.KEY)));
      for (final InputFile inputFile : inputFiles) {
        final String analysisFile =
            java.nio.file.Paths.get(inputFile.uri()).toAbsolutePath().toString();

        // skip reporting temp files
        if (analysisFile.contains(".scannerwork")) {
          continue;
        }

        service.submit(
            () -> analyzeFile(context, inputFile, scriptFile, timeoutSeconds, writeGuard));
      }
      waitForTermination(service, timeoutSeconds);
    } finally {
      if (!service.isTerminated()) {
        service.shutdownNow();
      }
    }
  }

  private void analyzeFile(
      SensorContext context,
      InputFile inputFile,
      File scriptFile,
      long timeoutSeconds,
      ContextWriteGuard writeGuard) {
    try {
      final String analysisFile =
          java.nio.file.Paths.get(inputFile.uri()).toAbsolutePath().toString();
      final String resultsFile = folder.newFile().toPath().toFile().getAbsolutePath();

      PowershellScriptExecutor.ExecutionResult result =
          createExecutor(scriptFile)
              .withArgument("-inputFile")
              .withPathArgument(analysisFile)
              .withArgument("-output")
              .withPathArgument(resultsFile)
              .withTimeout(timeoutSeconds, TimeUnit.SECONDS)
              .build()
              .execute();

      if (!result.isSuccess()) {
        if (result.isTimedOut()) {
          LOGGER.warn("Tokenizer timed out after {}s on {}.", timeoutSeconds, analysisFile);
        } else {
          LOGGER.warn("Tokenizer failed on {} with {}.", analysisFile, result);
        }
        return;
      }

      final File tokensFile = new File(resultsFile);
      if (!tokensFile.exists() || tokensFile.length() <= 0) {
        LOGGER.warn(
            "Tokenizer did not run successfully on {} file. Please check file contents.",
            analysisFile);
        return;
      }

      final Tokens tokens = reader.read(tokensFile);
      for (final IFiller filler : fillers) {
        filler.fill(context, inputFile, tokens, writeGuard);
      }
      LOGGER.debug("Running analysis for {} to {} finished.", analysisFile, resultsFile);
    } catch (final Exception e) {
      LOGGER.warn("Unexpected exception while running tokenizer on {}", inputFile, e);
    }
  }

  private void waitForTermination(ExecutorService service, long timeoutSeconds) {
    try {
      LOGGER.info("Waiting for file analysis to finish for {} seconds", timeoutSeconds);
      service.shutdown();
      boolean terminated = service.awaitTermination(timeoutSeconds, TimeUnit.SECONDS);
      if (!terminated) {
        LOGGER.warn(
            "Executor service did not terminate within {} seconds, forcing shutdownNow()",
            timeoutSeconds);
        service.shutdownNow();
      }
    } catch (final InterruptedException e) {
      LOGGER.warn("Unexpected error while waiting for executor service to finish", e);
      service.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
