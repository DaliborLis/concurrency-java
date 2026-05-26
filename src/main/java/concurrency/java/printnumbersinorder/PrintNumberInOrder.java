package concurrency.java.printnumbersinorder;


import java.util.stream.IntStream;

public class PrintNumberInOrder {

    private static final int THREAD_COUNT = 3;

    private static final Object lock = new Object();
    private static int counter = 0;

    public static void printSequence(int threadId) {

        for (int i = 0; i < 10; i++) {
            synchronized (lock) {
                while (counter % THREAD_COUNT != threadId) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.printf("Thread %d - %d%n", threadId, counter);
                counter++;
                lock.notifyAll();
            }
        }
    }

    static void main() {
        IntStream.range(0, THREAD_COUNT).forEach(i -> new Thread(() -> printSequence(i)).start());
    }

}
