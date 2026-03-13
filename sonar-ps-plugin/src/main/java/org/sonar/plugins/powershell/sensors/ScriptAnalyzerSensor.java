package org.sonar.plugins.powershell.sensors;

import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.TempFolder;
import org.sonar.plugins.powershell.Constants;
import org.sonar.plugins.powershell.fillers.IssuesFiller;
import org.sonar.plugins.powershell.issues.PsIssue;
import org.sonar.plugins.powershell.readers.IssuesReader;
import org.sonar.plugins.powershell.utils.PowershellScriptExecutor;

public class ScriptAnalyzerSensor extends BaseSensor {

  private final TempFolder folder;
  private final IssuesFiller issuesFiller;
  private final IssuesReader reader = new IssuesReader();

  private static final Logger LOGGER = LoggerFactory.getLogger(ScriptAnalyzerSensor.class);

  public ScriptAnalyzerSensor(final TempFolder folder, Configuration config) {
    super(config);
    this.folder = folder;
    this.issuesFiller = new IssuesFiller(config);
  }

  @Override
  protected void innerExecute(final SensorContext context) {
    try {
      final FileSystem fileSystem = context.fileSystem();
      final File baseDir = fileSystem.baseDir().getCanonicalFile();
      final String sourceDir = baseDir.getAbsolutePath();
      final String outFile = folder.newFile().toPath().toFile().getAbsolutePath();

      File scriptFile = prepareScript(folder, "/scriptAnalyzer.ps1", "scriptAnalyzer.ps1");
      PowershellScriptExecutor.Builder executorBuilder =
          createExecutor(scriptFile)
              .withArgument("-inputDir")
              .withPathArgument(sourceDir)
              .withArgument("-output")
              .withPathArgument(outFile)
              .withArgument("-minAnalyzerVersion")
              .withArgument(Constants.PS_ANALYZER_DEFAULT_VERSION)
              .withArgument("-autoInstall")
              .withArgument(String.valueOf(config.isPsAnalyzerAutoInstall()))
              .withArgument("-suffixes")
              .withArgument(String.join(",", config.getFileSuffixes()))
              .withLogStdout(true)
              .useInheritIO(false);

      LOGGER.info("Starting Script-Analyzer using powershell");
      PowershellScriptExecutor.ExecutionResult result = executorBuilder.build().execute();

      if (!result.isSuccess()) {
        LOGGER.error("PowerShell execution failed: {}", result);
        return;
      }

      final File outputFile = new File(outFile);
      if (!outputFile.exists() || outputFile.length() <= 0) {
        LOGGER.warn("Analysis finished with Exit Code 0 but output file is empty.");
        LOGGER.warn("PS STDOUT: {}", result.getStdOut());
        LOGGER.warn("PS STDERR: {}", result.getStdErr());
        return;
      }

      final List<PsIssue> issues = reader.read(outputFile);
      this.issuesFiller.fill(context, baseDir, issues);

      LOGGER.info("Script-Analyzer finished, found {} issues at {}", issues.size(), sourceDir);
    } catch (Exception e) {
      LOGGER.warn("Unexpected exception while running analysis", e);
    }
  }
}
