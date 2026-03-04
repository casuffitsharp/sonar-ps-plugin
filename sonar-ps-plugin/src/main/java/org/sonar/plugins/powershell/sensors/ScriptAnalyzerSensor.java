package org.sonar.plugins.powershell.sensors;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.TempFolder;
import org.sonar.plugins.powershell.fillers.IssuesFiller;
import org.sonar.plugins.powershell.issues.PsIssue;
import org.sonar.plugins.powershell.readers.IssuesReader;

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
        final Configuration config = context.config();
        final String powershellExecutable = config.get("sonar.ps.executable").orElse("powershell.exe");

        try {
            final File parserFile = folder.newFile("ps", "scriptAnalyzer.ps1");

            try {
                FileUtils.copyURLToFile(getClass().getResource("/scriptAnalyzer.ps1"), parserFile);
            } catch (final Throwable e1) {
                LOGGER.warn("Exception while copying tokenizer script", e1);
                return;
            }

            final String scriptFile = parserFile.getAbsolutePath();

            final FileSystem fileSystem = context.fileSystem();
            final File baseDir = fileSystem.baseDir();

            final String sourceDir = SystemUtils.IS_OS_WINDOWS
                    ? String.format("'%s'", baseDir.toPath().toFile().getAbsolutePath())
                    : baseDir.toPath().toFile().getAbsolutePath();

            final String outFile = folder.newFile().toPath().toFile().getAbsolutePath();

            final String[] args = new String[] { powershellExecutable, scriptFile, "-inputDir", sourceDir, "-output",
                    outFile };

            LOGGER.info(String.format("Starting Script-Analyzer using powershell: %s", Arrays.toString(args)));
            final Process process = new ProcessBuilder(args).inheritIO().start();

            final int pReturnValue = process.waitFor();

            if (pReturnValue != 0) {
                LOGGER.info(String.format(
                        "Error executing Powershell Script-Analyzer analyzer. Maybe Script-Analyzer is not installed? Error was: %s",
                        read(process)));
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
