package com.oracle.cda.semantic.data.producerconsumer;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ProducerConsumerApp {

    public static final String POISON_PILL = "__STOP__";

    static void main() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        CountDownLatch producerLatch = new CountDownLatch(5);
        CountDownLatch consumerLatch = new CountDownLatch(10);
        List<Producer> producers = IntStream.range(0, 3).mapToObj(i -> new Producer(queue, "Producer " + i, producerLatch)).toList();
        List<Consumer> consumers = IntStream.range(0, 5).mapToObj(i -> new Consumer(queue, "Consumer " + i, consumerLatch)).toList();

        try (ExecutorService exec = Executors.newFixedThreadPool(producers.size() + consumers.size())) {
            Stream.concat(producers.stream(), consumers.stream()).forEach(exec::submit);

            producerLatch.await();
            consumerLatch.await();
            exec.shutdownNow();

            exec.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
