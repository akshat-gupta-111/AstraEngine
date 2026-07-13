package engine.core;

import java.util.List;

import engine.cache.EngineMemoryCache;
import engine.interfaces.ICache;
import engine.interfaces.ILLMClient;
import engine.interfaces.IRetryManager;
import engine.interfaces.ITaskDispatcher;
import engine.models.EngineTask;
import engine.network.GeminiLLMClient;

public class WorkStealingDispatcher implements ITaskDispatcher {

    private class AstraWorker extends Thread {
        // Every physical thread gets its OWN private deque!
        public final WorkStealingDeque localQueue = new WorkStealingDeque();

        // We pass a reference to ALL workers so this thread knows who to steal from
        private final AstraWorker[] allWorkers;

        public AstraWorker(String name, AstraWorker[] allWorkers) {
            super(name);
            this.allWorkers = allWorkers;
        }

        @Override
        public void run() {

            while (true) {
                try {

                    EngineTask myTask = this.localQueue.removeFront();

                    if (myTask == null) {
                        for (AstraWorker victim : allWorkers) {

                            if (victim != this) {
                                if (this.localQueue.stealFrom(victim.localQueue)) {
                                    break;
                                }
                            }
                        }
                        myTask = this.localQueue.removeFront();

                    }
                    if (myTask != null) {

                        // 3. ACTUAL EXECUTION (Network + Cache + Barrier Release)
                        System.out.println("[THREAD " + this.getName() + "] Executing task: " + myTask.getTaskName());

                        StringBuilder contextBuilder = new StringBuilder();
                        if (myTask.getInputCacheKey() != null) {
                            for (String key : myTask.getInputCacheKey()) {
                                String cachedValue = memory.get(key);
                                if (cachedValue != null) {
                                    contextBuilder.append(cachedValue).append("\n");
                                }
                            }
                        }

                        String context = contextBuilder.toString().trim();
                        // Variable must be effectively final for the lambda block!
                        final String finalPrompt = myTask.getPrompt() + (context.isEmpty() ? "" : "\n" + context);

                        try {
                            int tokenCount = engine.tokenizer.SystemTokenizer.tokenizer(finalPrompt);
                            System.out
                                    .println("[VALVE] Task " + myTask.getTaskName() + " payload size: " + tokenCount
                                            + " tokens.");

                            if (tokenCount > 150) {
                                throw new RuntimeException(
                                        "SAFETY ABORT: Task " + myTask.getTaskName() + " exceeded token limit! ("
                                                + tokenCount + " > 150)");
                            }
                            String response = retryManager.executeWithRetry(3, () -> {
                                return llmClient.generateResponse(finalPrompt);
                            });

                            if (myTask.getOutputCacheKey() != null) {
                                memory.put(myTask.getOutputCacheKey(), response);
                            }
                        } catch (Exception e) {
                            System.out.println(this.getName() + " failed to execute " + myTask.getTaskName() + ": "
                                    + e.getMessage());
                        } finally {
                            // CRITICAL: We MUST lower the barrier, even if the network fails!
                            // If we don't do this, Kahn's algorithm will wait at .await() for eternity!
                            if (layerBarrier != null) {
                                layerBarrier.countDown();
                            }
                        }
                    } else {

                        Thread.sleep(10);
                    }

                } catch (InterruptedException e) {
                    System.out.println(this.getName() + " shutting down.");
                    break;
                }
            }
        }
    }

    private final AstraWorker[] workers;
    private final ILLMClient llmClient;
    private final ICache memory;
    private final IRetryManager retryManager;
    private java.util.concurrent.CountDownLatch layerBarrier;

    public WorkStealingDispatcher(int threadCount, ILLMClient llmClient, ICache memory, IRetryManager retryManager) {
        this.workers = new AstraWorker[threadCount];
        for (int i = 0; i < threadCount; i++) {
            workers[i] = new AstraWorker("Astra-Worker-" + i, this.workers);
        }

        for (int i = 0; i < threadCount; i++) {
            workers[i].start();
        }
        this.llmClient = llmClient;
        this.memory = memory;
        this.retryManager = retryManager;
    }

    public int[] dispatchParallelBatch(List<EngineTask> layer) {
        this.layerBarrier = new java.util.concurrent.CountDownLatch(layer.size());
        for (int i = 0; i < layer.size(); i++) {
            workers[i % workers.length].localQueue.addRear(layer.get(i));
        }
        try {
            layerBarrier.await();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new int[layer.size()];
    }

    public void shutdown() {
        for (AstraWorker worker : workers) {
            worker.interrupt();
        }
    }
}
