package com.oracle.cda.semantic.data.parallelfilereading;

import java.nio.file.Path;
import java.util.List;

/**
 * Parallel File Processor
 */
public class ParallelFileReadingApp {
    static void main() throws InterruptedException {
        ParallelFileReader fileReader = new ParallelFileReader(List.of(
                Path.of("/Users/dlis/ohai/ehr/ConcurrencyRefresh/src/main/resources/file1.txt"),
                Path.of("/Users/dlis/ohai/ehr/ConcurrencyRefresh/src/main/resources/file2.txt"),
                Path.of("/Users/dlis/ohai/ehr/ConcurrencyRefresh/src/main/resources/file3.txt"),
                Path.of("/Users/dlis/ohai/ehr/ConcurrencyRefresh/src/main/resources/file4.txt")
        ));
        System.out.println(fileReader.readAllLinesCompletionService());
    }
}
