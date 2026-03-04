package org.sonar.plugins.powershell.fillers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class ContextWriteGuardTest {

  @Test
  public void write_should_be_mutually_exclusive_without_sleep() throws Exception {
    ContextWriteGuard guard = new ContextWriteGuard();

    int threads = 8;
    ExecutorService pool = Executors.newFixedThreadPool(threads);
    try {
      CyclicBarrier startBarrier = new CyclicBarrier(threads);

      AtomicInteger inCritical = new AtomicInteger(0);
      AtomicBoolean overlapDetected = new AtomicBoolean(false);

      CountDownLatch firstEntered = new CountDownLatch(1);
      CountDownLatch releaseFirst = new CountDownLatch(1);

      Callable<Void> task = () -> {
        startBarrier.await(5, TimeUnit.SECONDS);

        guard.write(() -> {
          int now = inCritical.incrementAndGet();
          if (now > 1) {
            overlapDetected.set(true);
          }

          if (firstEntered.getCount() == 1) {
            firstEntered.countDown();

            try {
              releaseFirst.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
          }

          inCritical.decrementAndGet();
        });

        return null;
      };

      List<Future<Void>> futures = new ArrayList<>();
      for (int i = 0; i < threads; i++)
        futures.add(pool.submit(task));

      assertTrue("No thread entered the critical section in time", firstEntered.await(5, TimeUnit.SECONDS));

      releaseFirst.countDown();

      for (Future<Void> f : futures)
        f.get(10, TimeUnit.SECONDS);

      assertFalse("Critical section executed concurrently", overlapDetected.get());
      assertEquals("Counter should end at 0", 0, inCritical.get());
    } finally {
      pool.shutdownNow();
      pool.awaitTermination(10, TimeUnit.SECONDS);
    }
  }
}