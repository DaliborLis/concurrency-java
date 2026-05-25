package com.oracle.cda.semantic.data.producerconsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Producer implements Runnable {
    private final String name;
    private final BlockingQueue<String> queue;
    private final CountDownLatch latch;
    private final Random rand = new Random();

    public Producer(BlockingQueue<String> queue, String name, CountDownLatch latch) {
        this.name = name;
        this.queue = queue;
        this.latch = latch;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                queue.put(produce());
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private String produce() {
        return "Producer %s, message: %s, %s".formatted(name, System.currentTimeMillis(), rand.nextInt());
    }
}
