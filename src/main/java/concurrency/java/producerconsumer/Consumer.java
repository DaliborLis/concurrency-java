package concurrency.java.producerconsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import static concurrency.java.producerconsumer.ProducerConsumerApp.POISON_PILL;

public class Consumer implements Runnable {

    private final BlockingQueue<String> queue;
    private final String name;
    private final CountDownLatch latch;

    public Consumer(BlockingQueue<String> queue, String name, CountDownLatch latch) {
        this.queue = queue;
        this.name = name;
        this.latch = latch;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String message = queue.take();
                if (POISON_PILL.equals(message)) {
                    break;
                }
                consume(message);
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void consume(String message) {
        System.out.printf("%s -  Consumed message: %s%n", name, message);
    }

}
