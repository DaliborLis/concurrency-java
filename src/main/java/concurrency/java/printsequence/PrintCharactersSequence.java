package concurrency.java.printsequence;

import java.util.stream.IntStream;

public class PrintCharactersSequence {

    private static final int[] SEQUENCE = {0, 1, 0, 2};  // A, B, A, C
    private static final char[] TOKENS = new char[]{'A', 'B', 'C'};

    public static final int REPEAT_COUNT = 2;
    public static final int THREADS_COUNT = 3;

    private static int position = 0;
    private static final Object LOCK = new Object();

    public static void print(int threadId) {
        while (true) {
            synchronized (LOCK) {
                while (position < SEQUENCE.length * REPEAT_COUNT &&
                        SEQUENCE[position % SEQUENCE.length] != threadId) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (position >= SEQUENCE.length * REPEAT_COUNT) {
                    LOCK.notifyAll();
                    return;
                }
                System.out.printf("Thread %d - %s%n", threadId, TOKENS[threadId]);
                position++;
                LOCK.notifyAll();
            }
        }
    }

    static void main() {
        IntStream.range(0, THREADS_COUNT).forEach(i -> new Thread(() -> print(i)).start());
    }
}
