package org.sonar.plugins.powershell.fillers;

import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.plugins.powershell.Constants;
import org.sonar.plugins.powershell.PluginConfiguration;
import org.sonar.plugins.powershell.issues.PsIssue;

public class IssuesFiller {

  private static final Logger LOGGER = LoggerFactory.getLogger(IssuesFiller.class);
  private final PluginConfiguration config;

  public IssuesFiller(PluginConfiguration config) {
    this.config = config;
  }

  public void fill(final SensorContext context, final File sourceDir, final List<PsIssue> issues) {
    final FileSystem fileSystem = context.fileSystem();
    final String[] skipRulesArray = config.getExternalRulesSkipList();
    final java.util.Set<String> skipRules = new java.util.HashSet<>();
    for (String s : skipRulesArray) {
      skipRules.add(s.toLowerCase().trim());
    }
    for (final PsIssue issue : issues) {
      try {
        final String ruleName = issue.ruleId();
        final String repoKey = Constants.REPO_KEY;
        final String uniqueId = repoKey + ":" + ruleName;
        final String initialFile = issue.file();

        if (skipRules.contains(uniqueId.toLowerCase()) || initialFile.contains(".scannerwork")) {
          continue;
        }

        final String fsFile = new PathResolver().relativePath(sourceDir, new File(initialFile));
        final String message = issue.message();
        int issueLine = issue.line();
        final RuleKey ruleKey = RuleKey.of(repoKey, ruleName);

        final org.sonar.api.batch.fs.InputFile file =
            fileSystem.inputFile(
                fileSystem.predicates().and(fileSystem.predicates().hasRelativePath(fsFile)));

        if (file == null) {
          LOGGER.debug("File '{}' not found in system to add issue {}", initialFile, ruleKey);
          continue;
        }

        final NewIssue newIssue = context.newIssue().forRule(ruleKey);
        final NewIssueLocation loc = newIssue.newLocation().message(message).on(file);
        if (issueLine > 0) {
          loc.at(file.selectLine(issueLine));
        }
        newIssue.at(loc);
        newIssue.save();
      } catch (final Exception e) {
        LOGGER.warn("Unexpected exception while adding issue", e);
      }
    }
  }
}
