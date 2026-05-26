package concurrency.java.printnumbersinorder;

import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class PrintNumbersInOrderSemaphore {

    public static final int THREAD_COUNT = 3;
    private static final int LOOP_COUNT = 10;
    // Safe because semaphore handoff guarantees only one thread prints/increments at a time.
    private static int counter = 0;

    private static final Semaphore[] semaphores = new Semaphore[THREAD_COUNT];

    static {
        for (int i = 0; i < THREAD_COUNT; i++) {
            semaphores[i] = new Semaphore(0);
        }
        semaphores[0].release();
    }

    public static void printSequence(int threadId) {
        for (int i = 0; i < LOOP_COUNT; i++) {
            try {
                semaphores[threadId].acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            System.out.printf("Thread %d - %d%n", threadId, counter);
            counter++;
            semaphores[(threadId + 1) % THREAD_COUNT].release();
        }
    }

    static void main() {
        IntStream.range(0, THREAD_COUNT).forEach(i -> new Thread(() -> printSequence(i)).start());
    }

}
