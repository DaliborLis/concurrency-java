package com.oracle.cda.semantic.data.threadsafecounter;

public class CounterSynchronized implements Counter {
    private volatile int count = 0;

    @Override
    public synchronized void increment() {
        count++;
    }

    @Override
    public synchronized void decrement() {
        count--;
    }

    @Override
    public synchronized int get() {
        return count;
    }
}
