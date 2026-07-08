package concurrency.java.threadsafecounter;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class CustomAtomicInteger {

    private volatile int value;

    private static final VarHandle VALUE;

    static {
        try {
            VALUE = MethodHandles.lookup()
                    .findVarHandle(CustomAtomicInteger.class, "value", int.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public CustomAtomicInteger() {
    }

    public CustomAtomicInteger(int initialValue) {
        this.value = initialValue;
    }

    public int get() {
        return value;
    }

    public void set(int newValue) {
        this.value = newValue;
    }

    public void lazySet(int newValue) {
        VALUE.setRelease(this, newValue);
    }

    public boolean compareAndSet(int expectedValue, int newValue) {
        return VALUE.compareAndSet(this, expectedValue, newValue);
    }

    public int getAndIncrement() {
        while (true) { // CAS loop
            int oldValue = get();
            int newValue = oldValue + 1;

            if (compareAndSet(oldValue, newValue)) {
                return oldValue;
            }
        }
    }

    public int incrementAndGet() {
        while (true) { // CAS loop
            int oldValue = get();
            int newValue = oldValue + 1;

            if (compareAndSet(oldValue, newValue)) {
                return newValue;
            }
        }
    }

    public int getAndAdd(int delta) {
        return (int) VALUE.getAndAdd(this, delta);
    }

    public int addAndGet(int delta) {
        return (int) VALUE.getAndAdd(this, delta) + delta;
    }

    public int incrementAndGetOptimized() {
        return addAndGet(1);
    }
}