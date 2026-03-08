package org.sonar.plugins.powershell;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.impl.utils.JUnitTempFolder;
import org.sonar.plugins.powershell.sensors.ScriptAnalyzerSensor;

public class ScriptAnalyzerSensorTest {

  @Rule public TemporaryFolder folder = new TemporaryFolder();

  @org.junit.Rule public JUnitTempFolder temp = new JUnitTempFolder();

  @Test
  public void testExecuteAndFindIssues() throws IOException {
    SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
    if (SystemUtils.IS_OS_WINDOWS) {
      ctxTester.settings().setProperty(Constants.PS_EXECUTABLE, "powershell.exe");
    } else {
      ctxTester.settings().setProperty(Constants.PS_EXECUTABLE, "pwsh");
    }
    File baseFile = folder.newFile("test.ps1");
    FileUtils.copyURLToFile(getClass().getResource("/testFiles/test.ps1"), baseFile);
    DefaultInputFile ti =
        new TestInputFileBuilder("test", "test.ps1")
            .initMetadata(new String(Files.readAllBytes(baseFile.toPath())))
            .build();
    ctxTester.fileSystem().add(ti);

    ScriptAnalyzerSensor s = new ScriptAnalyzerSensor(temp, ctxTester.config());
    s.execute(ctxTester);

    Assert.assertEquals(4, ctxTester.allIssues().size());
  }

  @Test
  public void testExecuteAndSkipReporting() throws IOException {
    SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
    if (SystemUtils.IS_OS_WINDOWS) {
      ctxTester.settings().setProperty(Constants.PS_EXECUTABLE, "powershell.exe");
    } else {
      ctxTester.settings().setProperty(Constants.PS_EXECUTABLE, "pwsh");
    }
    // test.ps1 only contains PSAvoidUsingPositionalParameters issues.
    // Skipping this specific rule should result in 0 issues.
    ctxTester
        .settings()
        .setProperty(
            Constants.EXTERNAL_RULES_SKIP_LIST, "ps-psanalyzer:PSAvoidUsingPositionalParameters");
    File baseFile = folder.newFile("test.ps1");
    FileUtils.copyURLToFile(getClass().getResource("/testFiles/test.ps1"), baseFile);
    DefaultInputFile ti =
        new TestInputFileBuilder("test", "test.ps1")
            .initMetadata(new String(Files.readAllBytes(baseFile.toPath())))
            .build();
    ctxTester.fileSystem().add(ti);

    ScriptAnalyzerSensor s = new ScriptAnalyzerSensor(temp, ctxTester.config());
    s.execute(ctxTester);

    Assert.assertEquals(0, ctxTester.allIssues().size());
  }

  @Test
  public void shouldSkipWhenPluginDisabled() {
    SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
    ctxTester.settings().setProperty(Constants.SKIP_PLUGIN, "true");

    ScriptAnalyzerSensor s = new ScriptAnalyzerSensor(temp, ctxTester.config());
    s.execute(ctxTester);

    Assert.assertEquals(0, ctxTester.allIssues().size());
  }
}
