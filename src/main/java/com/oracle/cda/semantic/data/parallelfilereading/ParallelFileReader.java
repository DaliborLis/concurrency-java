package com.oracle.cda.semantic.data.parallelfilereading;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ParallelFileReader {

    private final List<Path> paths;

    public ParallelFileReader(List<Path> paths) {
        this.paths = paths;
    }

    public String readFilesInParallel() throws InterruptedException {
        try (ExecutorService exec = Executors.newFixedThreadPool(paths.size())) {
            List<Future<String>> futures = paths.stream().map(p -> exec.submit(() -> Files.readString(p))).toList();
            exec.shutdown();
            exec.awaitTermination(1, TimeUnit.MINUTES);
            return futures.stream().map(f -> {
                try {
                    return f.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.joining(System.lineSeparator()));

        }
    }

    public String readFilesCompletableFuture() {
        List<CompletableFuture<String>> futures = paths.stream().map(p -> CompletableFuture.supplyAsync(() -> {
            try {
                return Files.readString(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        })).toList();
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        return futures.stream().map(CompletableFuture::join).collect(Collectors.joining(System.lineSeparator()));
    }

    public String readAllLinesCompletionService() {
        try (ExecutorService exec = Executors.newFixedThreadPool(10)) {
            CompletionService<String> ecs = new ExecutorCompletionService<>(exec);
            List<Future<String>> futures = paths.stream().map(p -> ecs.submit(() -> Files.readString(p))).toList();
            List<String> results = new ArrayList<>(futures.size());
            for (int i =0; i < futures.size(); i++) {
                try {
                    results.add(ecs.poll(1, TimeUnit.MINUTES).get());
                } catch (ExecutionException e) {
                    futures.forEach(f -> f.cancel(true));
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    futures.forEach(f -> f.cancel(true));
                    throw new RuntimeException(e);
                }
            }
            return results.stream().collect(Collectors.joining(System.lineSeparator()));
        }

    }
}
