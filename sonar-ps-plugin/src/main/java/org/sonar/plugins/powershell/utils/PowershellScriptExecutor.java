package org.sonar.plugins.powershell.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Helper class to execute PowerShell scripts with a fluent Builder API. */
public class PowershellScriptExecutor {

  private static final Logger LOG = LoggerFactory.getLogger(PowershellScriptExecutor.class);
  private static final int FORCED_TERMINATION_GRACE_PERIOD_SECONDS = 5;

  @FunctionalInterface
  public interface ProcessStarter {
    Process start(List<String> command, boolean inheritIO) throws IOException;
  }

  private static final ProcessStarter DEFAULT_STARTER =
      (cmd, inherit) -> {
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
  private final boolean logStdout;
  private final ProcessStarter processStarter;

  private PowershellScriptExecutor(Builder builder) {
    this.executable = builder.executable;
    this.scriptFile = builder.scriptFile;
    this.arguments = new ArrayList<>(builder.arguments);
    this.timeout = builder.timeout;
    this.timeoutUnit = builder.timeoutUnit;
    this.inheritIO = builder.inheritIO;
    this.logStdout = builder.logStdout;
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

    CompletableFuture<String> stdOutFuture = null;
    CompletableFuture<String> stdErrFuture = null;
    ExecutorService drainExecutor = null;

    try {
      if (!inheritIO) {
        // We manually manage the executor lifecycle instead of try-with-resources
        // because in Java 21, ExecutorService.close() (called by try-with-resources)
        // blocks indefinitely.
        // We want to ensure we respect our own timeout logic.
        @SuppressWarnings("java:S2095")
        final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        drainExecutor = executor;

        // Start draining streams in background using virtual threads.
        stdOutFuture = drainStream(process.getInputStream(), "stdout", executor);
        stdErrFuture = drainStream(process.getErrorStream(), "stderr", executor);
      }

      try {
        boolean finished;
        if (timeout > 0) {
          finished = process.waitFor(timeout, timeoutUnit);
        } else {
          process.waitFor();
          finished = true;
        }

        if (!finished) {
          LOG.warn(
              "PowerShell process timed out after {} {}. Killing it forcibly.",
              timeout,
              timeoutUnit);
          process.destroyForcibly();
          cleanupAfterForcedTermination(process, stdOutFuture, stdErrFuture);
          return ExecutionResult.timeout();
        }

        int exitCode = process.exitValue();
        String stdOut = "";
        String stdErr = "";

        if (!inheritIO && stdOutFuture != null && stdErrFuture != null) {
          stdOut = stdOutFuture.join();
          stdErr = stdErrFuture.join();
        }

        return new ExecutionResult(exitCode, stdOut, stdErr, false, false);
      } catch (InterruptedException e) {
        LOG.warn("PowerShell process interrupted. Killing it forcibly.");
        process.destroyForcibly();
        cleanupAfterForcedTermination(process, stdOutFuture, stdErrFuture);
        Thread.currentThread().interrupt();
        return ExecutionResult.interrupted();
      }
    } finally {
      if (drainExecutor != null) {
        drainExecutor.shutdown();
        try {
          if (!drainExecutor.awaitTermination(
              FORCED_TERMINATION_GRACE_PERIOD_SECONDS, TimeUnit.SECONDS)) {
            drainExecutor.shutdownNow();
          }
        } catch (InterruptedException e) {
          drainExecutor.shutdownNow();
          Thread.currentThread().interrupt();
        }
      }
    }
  }

  private CompletableFuture<String> drainStream(
      InputStream stream, String name, ExecutorService executor) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            return readStream(stream, name);
          } catch (IOException e) {
            LOG.warn("Error reading {}", name, e);
            return "";
          }
        },
        executor);
  }

  private void cleanupAfterForcedTermination(
      Process process,
      CompletableFuture<String> stdOutFuture,
      CompletableFuture<String> stdErrFuture) {
    try {
      process.waitFor(FORCED_TERMINATION_GRACE_PERIOD_SECONDS, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    if (!inheritIO) {
      closeQuietly(process.getInputStream());
      closeQuietly(process.getErrorStream());
      closeQuietly(process.getOutputStream());

      if (stdOutFuture != null) {
        stdOutFuture.cancel(true);
      }
      if (stdErrFuture != null) {
        stdErrFuture.cancel(true);
      }
    }
  }

  private void closeQuietly(InputStream stream) {
    try {
      stream.close();
    } catch (IOException e) {
      LOG.debug("Failed to close stream quietly", e);
    }
  }

  private void closeQuietly(java.io.OutputStream stream) {
    try {
      stream.close();
    } catch (IOException e) {
      LOG.debug("Failed to close stream quietly", e);
    }
  }

  private String readStream(InputStream stream, String streamName) throws IOException {
    StringBuilder builder = new StringBuilder();
    String upperStreamName = streamName.toUpperCase(java.util.Locale.ROOT);
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line).append(System.lineSeparator());
        if ("stdout".equalsIgnoreCase(streamName) && logStdout && LOG.isInfoEnabled()) {
          LOG.info("[{}] {}", upperStreamName, line);
        } else if ("stderr".equalsIgnoreCase(streamName) && LOG.isDebugEnabled()) {
          LOG.debug("[{}] {}", upperStreamName, line);
        }
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
    private boolean logStdout = false;
    private ProcessStarter processStarter = DEFAULT_STARTER;

    public Builder withLogStdout(boolean logStdout) {
      this.logStdout = logStdout;
      return this;
    }

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

    /**
     * Adds an argument that represents a file path. ProcessBuilder handles necessary quoting for
     * spaces.
     */
    public Builder withPathArgument(String path) {
      return withArgument(path);
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

  /** Result of the PowerShell execution. */
  public static class ExecutionResult {

    private final int exitCode;
    private final String stdOut;
    private final String stdErr;
    private final boolean timedOut;
    private final boolean interrupted;

    private ExecutionResult(
        int exitCode, String stdOut, String stdErr, boolean timedOut, boolean interrupted) {
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

    public int getExitCode() {
      return exitCode;
    }

    public String getStdOut() {
      return stdOut;
    }

    public String getStdErr() {
      return stdErr;
    }

    public boolean isTimedOut() {
      return timedOut;
    }

    public boolean isInterrupted() {
      return interrupted;
    }

    public boolean isSuccess() {
      return !timedOut && !interrupted && exitCode == 0;
    }

    @Override
    public String toString() {
      return String.format(
          "ExitCode: %d, TimedOut: %b, Interrupted: %b, StdOut: [%s], StdErr: [%s]",
          exitCode, timedOut, interrupted, truncate(stdOut), truncate(stdErr));
    }

    private String truncate(String s) {
      if (s == null || s.length() <= 500) {
        return s;
      }
      return s.substring(0, 500) + "... [TRUNCATED]";
    }
  }
}
