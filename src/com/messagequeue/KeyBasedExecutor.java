package com.messagequeue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class KeyBasedExecutor {
	private Executor[] executors;

	public KeyBasedExecutor(int threads) {
		executors = new Executor[threads];
		for(Executor executor : executors)
			executor = Executors.newSingleThreadExecutor();
	}
	
	public CompletionStage<Void> submit(String id, Runnable task){
		return CompletableFuture.runAsync(task, executors[id.hashCode()%executors.length]);
	}
	
	public <T> CompletionStage<T> get(String id, Supplier<T> task){
		return CompletableFuture.supplyAsync(task, executors[id.hashCode()%executors.length]);
	}
}
