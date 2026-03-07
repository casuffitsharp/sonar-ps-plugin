package org.sonar.plugins.powershell.utils;

public final class ContextWriteGuard {

  private final Object lock = new Object();

  public void write(Runnable r) {
    synchronized (lock) {
      r.run();
    }
  }
}
