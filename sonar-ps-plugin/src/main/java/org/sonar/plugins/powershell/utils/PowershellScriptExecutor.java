package org.sonar.plugins.powershell.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to execute PowerShell scripts with a fluent Builder API.
 */
public class PowershellScriptExecutor {
  private static final Logger LOG = LoggerFactory.getLogger(PowershellScriptExecutor.class);

  @FunctionalInterface
  public interface ProcessStarter {
    Process start(List<String> command, boolean inheritIO) throws IOException;
  }

  private static final ProcessStarter DEFAULT_STARTER = (cmd, inherit) -> {
    ProcessBuilder pb = new ProcessBuilder(cmd);
    if (inherit) {
      pb.inheritIO();
    }
    return pb.start();
  };

  private final String executable;
  private final File scriptFile;
  private final List<String> arguments;
  private final long timeout;
  private final TimeUnit timeoutUnit;
  private final boolean inheritIO;
  private final ProcessStarter processStarter;

  private PowershellScriptExecutor(Builder builder) {
    this.executable = builder.executable;
    this.scriptFile = builder.scriptFile;
    this.arguments = new ArrayList<>(builder.arguments);
    this.timeout = builder.timeout;
    this.timeoutUnit = builder.timeoutUnit;
    this.inheritIO = builder.inheritIO;
    this.processStarter = builder.processStarter;
  }

  public ExecutionResult execute() throws IOException {
    List<String> command = new ArrayList<>();
    command.add(executable);
    command.add("-ExecutionPolicy");
    command.add("Bypass");
    command.add("-File");
    command.add(scriptFile.getAbsolutePath());
    command.addAll(arguments);

    LOG.debug("Executing command: {}", command);
    Process process = processStarter.start(command, inheritIO);
    
    try {
      boolean finished;
      if (timeout > 0) {
        finished = process.waitFor(timeout, timeoutUnit);
      } else {
        process.waitFor();
        finished = true;
      }

      if (!finished) {
        LOG.warn("PowerShell process timed out after {} {}. Killing it forcibly.", timeout, timeoutUnit);
        process.destroyForcibly();
        // Wait briefly for the process to be terminated
        process.waitFor(5, TimeUnit.SECONDS); 
        return ExecutionResult.timeout();
      }

      int exitCode = process.exitValue();
      String stdOut = "";
      String stdErr = "";

      if (!inheritIO) {
        stdOut = readStream(process.getInputStream());
        stdErr = readStream(process.getErrorStream());
      }

      return new ExecutionResult(exitCode, stdOut, stdErr, false, false);

    } catch (InterruptedException e) {
      LOG.warn("PowerShell process interrupted. Killing it forcibly.", e);
      process.destroyForcibly();
      Thread.currentThread().interrupt();
      return ExecutionResult.interrupted();
    }
  }

  private String readStream(InputStream stream) throws IOException {
    StringBuilder builder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line).append(System.lineSeparator());
      }
    }
    return builder.toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String executable = SystemUtils.IS_OS_WINDOWS ? "powershell.exe" : "pwsh";
    private File scriptFile;
    private final List<String> arguments = new ArrayList<>();
    private long timeout = 0;
    private TimeUnit timeoutUnit = TimeUnit.SECONDS;
    private boolean inheritIO = true;
    private ProcessStarter processStarter = DEFAULT_STARTER;

    public Builder withExecutable(String executable) {
      this.executable = executable;
      return this;
    }

    public Builder withScriptFile(File scriptFile) {
      this.scriptFile = scriptFile;
      return this;
    }

    public Builder withArgument(String argument) {
      this.arguments.add(argument);
      return this;
    }

    public Builder withArguments(String... args) {
      this.arguments.addAll(Arrays.asList(args));
      return this;
    }

    public Builder withPathArgument(String path) {
        this.arguments.add(path);
        return this;
    }

    public Builder withTimeout(long timeout, TimeUnit unit) {
      this.timeout = timeout;
      this.timeoutUnit = unit;
      return this;
    }

    public Builder useInheritIO(boolean inheritIO) {
      this.inheritIO = inheritIO;
      return this;
    }

    // Package-private for testing
    Builder withProcessStarter(ProcessStarter starter) {
      this.processStarter = starter;
      return this;
    }

    public PowershellScriptExecutor build() {
      if (scriptFile == null) {
        throw new IllegalStateException("Script file must be provided");
      }
      return new PowershellScriptExecutor(this);
    }
  }

  /**
   * Result of the PowerShell execution.
   */
  public static class ExecutionResult {
    private final int exitCode;
    private final String stdOut;
    private final String stdErr;
    private final boolean timedOut;
    private final boolean interrupted;

    private ExecutionResult(int exitCode, String stdOut, String stdErr, boolean timedOut, boolean interrupted) {
      this.exitCode = exitCode;
      this.stdOut = stdOut;
      this.stdErr = stdErr;
      this.timedOut = timedOut;
      this.interrupted = interrupted;
    }

    public static ExecutionResult timeout() {
      return new ExecutionResult(-1, "", "", true, false);
    }

    public static ExecutionResult interrupted() {
      return new ExecutionResult(-1, "", "", false, true);
    }

    public int getExitCode() { return exitCode; }
    public String getStdOut() { return stdOut; }
    public String getStdErr() { return stdErr; }
    public boolean isTimedOut() { return timedOut; }
    public boolean isInterrupted() { return interrupted; }
    public boolean isSuccess() { return !timedOut && !interrupted && exitCode == 0; }
    
    @Override
    public String toString() {
        return String.format("ExitCode: %d, TimedOut: %b, Interrupted: %b", exitCode, timedOut, interrupted);
    }
  }
}
