package com.oracle.cda.semantic.data.threadsafecounter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Thread-safe Counter
 */
public class App {

    static void main() {
        Counter counter = new ReentrantLockCounter();
        try (ExecutorService exec = Executors.newFixedThreadPool(15)) {
            IntStream.range(0, 7).forEach(i -> {
                exec.submit(() -> {
                    if (i % 2 == 0) {
                        counter.increment();
                    } else {
                        counter.decrement();
                    }
                    System.out.println("Thread: " + i);
                });
            });
            exec.shutdown();
            exec.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(counter.get());

    }
}
