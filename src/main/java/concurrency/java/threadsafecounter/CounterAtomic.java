package com.oracle.cda.semantic.data.threadsafecounter;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterAtomic implements Counter {

    private final AtomicInteger count = new AtomicInteger();

    @Override
    public void increment() {
        count.incrementAndGet();
    }

    @Override
    public void decrement() {
        count.decrementAndGet();
    }

    @Override
    public int get() {
        return count.get();
    }
}
