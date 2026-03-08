package org.sonar.plugins.powershell.fillers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.config.Configuration;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.powershell.Constants;
import org.sonar.plugins.powershell.issues.PsIssue;

public class IssuesFillerTest {

  @Rule public TemporaryFolder folder = new TemporaryFolder();

  private SensorContext context;
  private FileSystem fileSystem;
  private FilePredicates predicates;
  private Configuration configuration;
  private NewIssue newIssue;
  private NewIssueLocation location;
  private IssuesFiller sut;

  @Before
  public void setUp() {
    context = mock(SensorContext.class);
    fileSystem = mock(FileSystem.class);
    predicates = mock(FilePredicates.class);
    configuration = mock(Configuration.class);
    newIssue = mock(NewIssue.class);
    location = mock(NewIssueLocation.class);
    sut = new IssuesFiller(configuration);

    when(context.fileSystem()).thenReturn(fileSystem);
    when(fileSystem.predicates()).thenReturn(predicates);
    when(predicates.hasRelativePath(any()))
        .thenReturn(mock(org.sonar.api.batch.fs.FilePredicate.class));
    when(context.newIssue()).thenReturn(newIssue);
    when(newIssue.forRule(any())).thenReturn(newIssue);
    when(newIssue.newLocation()).thenReturn(location);
    when(location.message(any())).thenReturn(location);
    when(location.on(any())).thenReturn(location);
    when(location.at(any())).thenReturn(location);
    // By default, getStringArray returns empty array (no skip rules)
    when(configuration.getStringArray(Constants.EXTERNAL_RULES_SKIP_LIST))
        .thenReturn(new String[0]);
    when(fileSystem.baseDir()).thenReturn(new File("."));
  }

  @Test
  public void shouldAddIssue() {
    File sourceFile = null;
    try {
      sourceFile = folder.newFile("test.ps1");
    } catch (java.io.IOException e) {
      throw new RuntimeException(e);
    }
    InputFile inputFile = mock(InputFile.class);
    when(fileSystem.inputFile(any())).thenReturn(inputFile);

    PsIssue issue = new PsIssue("RuleId", "message", 1, "MAJOR", sourceFile.getAbsolutePath());
    List<PsIssue> issues = Arrays.asList(issue);

    sut.fill(context, folder.getRoot(), issues);

    verify(newIssue).forRule(RuleKey.of(Constants.REPO_KEY, "RuleId"));
    verify(newIssue).save();
  }

  @Test
  public void shouldSkipRulesInSkipList() {
    when(configuration.getStringArray(Constants.EXTERNAL_RULES_SKIP_LIST))
        .thenReturn(new String[] {Constants.REPO_KEY + ":RuleId"});

    PsIssue issue = new PsIssue("RuleId", "message", 1, "MAJOR", "test.ps1");
    List<PsIssue> issues = Arrays.asList(issue);

    sut.fill(context, folder.getRoot(), issues);

    verify(newIssue, never()).save();
  }

  @Test
  public void shouldSkipTempFiles() {
    PsIssue issue = new PsIssue("RuleId", "message", 1, "MAJOR", "path/with/.scannerwork/file.ps1");
    List<PsIssue> issues = Arrays.asList(issue);

    sut.fill(context, folder.getRoot(), issues);

    verify(newIssue, never()).save();
  }
}
