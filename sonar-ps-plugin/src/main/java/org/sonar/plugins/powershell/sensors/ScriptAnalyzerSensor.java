package org.sonar.plugins.powershell.sensors;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.TempFolder;
import org.sonar.plugins.powershell.fillers.IssuesFiller;
import org.sonar.plugins.powershell.issues.PsIssue;
import org.sonar.plugins.powershell.readers.IssuesReader;
import org.sonar.plugins.powershell.utils.PowershellScriptExecutor;

public class ScriptAnalyzerSensor extends BaseSensor {

    private final TempFolder folder;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptAnalyzerSensor.class);

    private final IssuesFiller issuesFiller = new IssuesFiller();
    private final IssuesReader reader = new IssuesReader();

    public ScriptAnalyzerSensor(final TempFolder folder) {
        this.folder = folder;
    }

    @Override
    protected void innerExecute(final SensorContext context) {
        try {
            final FileSystem fileSystem = context.fileSystem();
            final File baseDir = fileSystem.baseDir();
            final String sourceDir = baseDir.toPath().toFile().getAbsolutePath();
            final String outFile = folder.newFile().toPath().toFile().getAbsolutePath();

            File scriptFile = prepareScript(folder, "/scriptAnalyzer.ps1", "scriptAnalyzer.ps1");
            PowershellScriptExecutor.Builder executorBuilder = createExecutor(context, scriptFile)
                    .withArgument("-inputDir")
                    .withPathArgument(sourceDir)
                    .withArgument("-output")
                    .withPathArgument(outFile);

            LOGGER.info("Starting Script-Analyzer using powershell");
            PowershellScriptExecutor.ExecutionResult result = executorBuilder.build().execute();

            if (!result.isSuccess()) {
                LOGGER.info(String.format(
                        "Error executing Powershell Script-Analyzer analyzer. Maybe Script-Analyzer is not installed? %s",
                        result));
                return;
            }

            final File outputFile = new File(outFile);
            if (!outputFile.exists() || outputFile.length() <= 0) {
                LOGGER.warn("Analysis was not run ok, and output file was empty at: " + outFile);
                return;
            }

            final List<PsIssue> issues = reader.read(outputFile);
            this.issuesFiller.fill(context, baseDir, issues);

            LOGGER.info(String.format("Script-Analyzer finished, found %s issues at %s", issues.size(), sourceDir));

        } catch (Throwable e) {
            LOGGER.warn("Unexpected exception while running analysis", e);
        }

    }

}
