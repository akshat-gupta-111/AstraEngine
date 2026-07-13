package engine.core;

import engine.interfaces.ICache;
import engine.interfaces.ILLMClient;
import engine.interfaces.IRetryManager;
import engine.interfaces.ITaskDispatcher;
import engine.models.EngineTask;
import engine.tokenizer.SystemTokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultithreadedDispatcher implements ITaskDispatcher {

    private final ExecutorService threadPool;
    private final ILLMClient llmClient;
    private final ICache memory;
    private final IRetryManager retryManager;

    public MultithreadedDispatcher(ILLMClient llmClient, ICache memory, IRetryManager retryManager) {
        this.threadPool = Executors.newFixedThreadPool(4);
        this.llmClient = llmClient;
        this.memory = memory;
        this.retryManager = retryManager;

    }

    public int[] dispatchParallelBatch(List<EngineTask> layer) {
        int n = layer.size();
        int result[] = new int[n];

        List<Future<Integer>> futures = new ArrayList<>();

        for (EngineTask task : layer) {
            Future<Integer> future = threadPool.submit(() -> {

                System.out.println("[THREAD " + Thread.currentThread().getName() + "]Executing task: "
                        + task.getTaskName() + " | Prompt: " + task.getPrompt());
                long startTime = System.currentTimeMillis();
                StringBuilder contextBuilder = new StringBuilder();
                if (task.getInputCacheKey() != null) {
                    for (String key : task.getInputCacheKey()) {
                        String cachedValue = memory.get(key);
                        if (cachedValue != null) {
                            contextBuilder.append(cachedValue).append("\n");
                        }
                    }
                }

                String context = contextBuilder.toString().trim();
                // Variable must be effectively final for the lambda block!
                final String finalPrompt = task.getPrompt() + (context.isEmpty() ? "" : "\n" + context);

                int tokenCount = engine.tokenizer.SystemTokenizer.tokenizer(finalPrompt);
                System.out
                        .println("[VALVE] Task " + task.getTaskName() + " payload size: " + tokenCount + " tokens.");

                if (tokenCount > 5) {
                    throw new RuntimeException("SAFETY ABORT: Task " + task.getTaskName() + " exceeded token limit! ("
                            + tokenCount + " > 150)");
                }

                String response = retryManager.executeWithRetry(3, () -> {
                    return llmClient.generateResponse(finalPrompt);
                });
                if (task.getOutputCacheKey() != null) {
                    memory.put(task.getOutputCacheKey(), response);
                }
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                return (int) duration;
            });

            futures.add(future);
        }
        try {
            for (int i = 0; i < n; i++) {
                result[i] = futures.get(i).get();
            }
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted!");
            Thread.currentThread().interrupt(); // Restore the interrupted status!
        } catch (Exception e) {
            System.out.println("Execution failed: " + e);
            throw new RuntimeException("CRITICAL: A task in the parallel layer failed. Halting entire workflow!", e);
        }

        return result;
    }

    public void shutdown() {
        threadPool.shutdown();
    }
}
