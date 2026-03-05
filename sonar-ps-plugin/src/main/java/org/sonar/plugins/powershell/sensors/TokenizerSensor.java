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
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.TempFolder;
import org.sonar.plugins.powershell.Constants;
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

    private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();

    private final IFiller[] fillers = new IFiller[] { new LineMeasuresFiller(), new CpdFiller(),
            new HighlightingFiller(), new HalsteadComplexityFiller(), new CComplexityFiller() };
    private final TokensReader reader = new TokensReader();
    private final TempFolder folder;

    public TokenizerSensor(final TempFolder folder) {
        this.folder = folder;
    }

    @Override
    protected void innerExecute(final SensorContext context) {
        final Configuration config = context.config();
        long timeoutSeconds = config.get("sonar.ps.tokenizer.timeout").map(Long::parseLong).orElse(3600L);

        final boolean skipAnalysis = config.getBoolean(Constants.SKIP_TOKENIZER).orElse(false);
        if (skipAnalysis) {
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

        try (ExecutorService service = Executors.newWorkStealingPool()) {
            final Iterable<InputFile> inputFiles = fs.inputFiles(p.and(p.hasLanguage(PowershellLanguage.KEY)));
            for (final InputFile inputFile : inputFiles) {

                final String analysisFile = inputFile.filename();

                // skip reporting temp files
                if (analysisFile.contains(".scannerwork")) {
                    continue;
                }

                service.submit(() -> {
                    try {
                        final String resultsFile = folder.newFile().toPath().toFile().getAbsolutePath();

                        PowershellScriptExecutor.ExecutionResult result = createExecutor(context, scriptFile)
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
                            LOGGER.warn(String.format(
                                    "Tokenizer did not run successfully on %s file. Please check file contents.",
                                    analysisFile));
                            return;
                        }

                        final Tokens tokens = reader.read(tokensFile);
                        for (final IFiller filler : fillers) {
                            filler.fill(context, inputFile, tokens, writeGuard);
                        }
                        if (isDebugEnabled) {
                            LOGGER.debug(String.format("Running analysis for %s to %s finished.", analysisFile,
                                    resultsFile));
                        }
                    } catch (final Throwable e) {
                        LOGGER.warn(String.format("Unexpected exception while running tokenizer on %s", inputFile),
                                e);
                    }
                });

            }
            try {
                LOGGER.info("Waiting for file analysis to finish for " + timeoutSeconds + " seconds");
                service.shutdown();
                service.awaitTermination(timeoutSeconds, TimeUnit.SECONDS);
                service.shutdownNow();
            } catch (final InterruptedException e) {
                LOGGER.warn("Unexpected error while running waiting for executor service to finish", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
