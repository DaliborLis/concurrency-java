package com.oracle.cda.semantic.data.threadsafecounter;

public interface Counter {

   void increment();
   void decrement();
   int get();

}
