package org.sonar.plugins.powershell.issues;

import java.util.Objects;

public record PsIssue(String ruleId, String message, int line, String severity, String file) {
  public PsIssue {
    severity = Objects.requireNonNullElse(severity, "MAJOR");
  }
}
