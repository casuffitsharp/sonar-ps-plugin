package org.sonar.plugins.powershell.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Before;
import org.junit.Test;

public class PowershellScriptExecutorTest {

  private PowershellScriptExecutor.ProcessStarter processStarter;
  private TestProcess process;
  private File tempScript;

  @Before
  public void setUp() throws IOException {
    processStarter = mock(PowershellScriptExecutor.ProcessStarter.class);
    process = new TestProcess();
    tempScript = File.createTempFile("test", ".ps1");
    tempScript.deleteOnExit();

    when(processStarter.start(any(), anyBoolean())).thenReturn(process);
  }

  @Test
  public void shouldExecuteSuccessfully() throws Exception {
    process.exitValue = 0;
    process.stdOut = "output";
    process.stdErr = "error";

    PowershellScriptExecutor executor = PowershellScriptExecutor.builder()
        .withProcessStarter(processStarter)
        .withScriptFile(tempScript)
        .useInheritIO(false)
        .build();

    PowershellScriptExecutor.ExecutionResult result = executor.execute();

    assertTrue(result.isSuccess());
    assertEquals(0, result.getExitCode());
    assertEquals("output" + System.lineSeparator(), result.getStdOut());
    assertEquals("error" + System.lineSeparator(), result.getStdErr());
    assertFalse(result.isTimedOut());
    assertFalse(result.isInterrupted());
  }

  @Test
  public void shouldHandleTimeout() throws Exception {
    process.waitForResult = false;

    PowershellScriptExecutor executor = PowershellScriptExecutor.builder()
        .withProcessStarter(processStarter)
        .withScriptFile(tempScript)
        .withTimeout(1, TimeUnit.SECONDS)
        .build();

    PowershellScriptExecutor.ExecutionResult result = executor.execute();

    assertFalse(result.isSuccess());
    assertTrue(result.isTimedOut());
    assertTrue(process.destroyed.get());
  }

  @Test
  public void shouldHandleInterruption() throws Exception {
    process.shouldThrowInterruptedException = true;

    PowershellScriptExecutor executor = PowershellScriptExecutor.builder()
        .withProcessStarter(processStarter)
        .withScriptFile(tempScript)
        .build();

    PowershellScriptExecutor.ExecutionResult result = executor.execute();

    assertFalse(result.isSuccess());
    assertTrue(result.isInterrupted());
    assertTrue(Thread.currentThread().isInterrupted()); // Verify thread interrupt status was restored
    Thread.interrupted(); // Clear status for subsequent tests
    assertTrue(process.destroyed.get());
  }

  @Test(expected = IllegalStateException.class)
  public void shouldFailIfNoScriptProvided() {
    PowershellScriptExecutor.builder().build();
  }

  private static class TestProcess extends Process {
    int exitValue = 0;
    String stdOut = "";
    String stdErr = "";
    boolean waitForResult = true;
    boolean shouldThrowInterruptedException = false;
    AtomicBoolean destroyed = new AtomicBoolean(false);

    @Override
    public OutputStream getOutputStream() { return null; }
    @Override
    public InputStream getInputStream() { return new ByteArrayInputStream(stdOut.getBytes(StandardCharsets.UTF_8)); }
    @Override
    public InputStream getErrorStream() { return new ByteArrayInputStream(stdErr.getBytes(StandardCharsets.UTF_8)); }
    
    @Override
    public int waitFor() throws InterruptedException {
      if (shouldThrowInterruptedException) throw new InterruptedException();
      return exitValue;
    }

    @Override
    public boolean waitFor(long timeout, TimeUnit unit) throws InterruptedException {
        if (shouldThrowInterruptedException) throw new InterruptedException();
        return waitForResult;
    }

    @Override
    public int exitValue() { return exitValue; }
    @Override
    public void destroy() { destroyed.set(true); }
    
    @Override
    public Process destroyForcibly() {
        destroyed.set(true);
        return this;
    }
  }
}
