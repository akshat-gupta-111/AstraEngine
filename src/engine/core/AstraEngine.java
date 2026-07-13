package engine.core;

import engine.models.DAG;
import engine.models.EngineTask;
import engine.cache.EngineMemoryCache;
import engine.core.MultithreadedDispatcher;
import engine.core.TelemetryAnalyzer;
import engine.security.WorkflowScanner;
import engine.network.GeminiLLMClient;
import engine.network.GPTLLMClient;

import engine.network.RetryManager;

import engine.interfaces.*;

import java.util.*;

public class AstraEngine {

    IWorkflowScanner scanner;
    ITaskDispatcher dispatcher;
    ITelemetryAnalyzer analyzer;
    ICache memory;
    ILLMClient llmClient;
    IRetryManager retryManager;

    public AstraEngine() {
        this.scanner = new WorkflowScanner();
        // this.llmClient = new GeminiLLMClient();
        this.llmClient = new GPTLLMClient();
        this.retryManager = new RetryManager();
        this.memory = new EngineMemoryCache(10);
        this.analyzer = new TelemetryAnalyzer();
        // this.dispatcher = new MultithreadedDispatcher(this.llmClient, this.memory, this.retryManager);
        this.dispatcher = new WorkStealingDispatcher(4,this.llmClient, this.memory, this.retryManager);

    }

    public void runWorkflow(int totalTasks, List<List<Integer>> adjList, Map<Integer, EngineTask> registry) {
        try {
            scanner.cycleDetection(adjList, totalTasks);
        } catch (Exception e) {
            System.out.println("GOT : " + e);
            System.out.println("Exiting Safely!");
            dispatcher.shutdown();
            return;
        }
        Queue<Integer> queue = new LinkedList<>();
        int[] indegree = new int[totalTasks];

        for (int curr = 0; curr < totalTasks; curr++) {
            for (int child : adjList.get(curr)) {
                indegree[child]++;
            }
        }

        for (int node = 0; node < totalTasks; node++) {
            if (indegree[node] == 0) {
                queue.add(node);
            }
        }

        int currentLevel = 0;
        int taskExecuted = 0;

        try {
            while (!queue.isEmpty()) {
                int size = queue.size();
                List<EngineTask> currentLayerTasks = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    int task = queue.poll();
                    EngineTask workerTask = registry.get(task);
                    if (workerTask == null) {
                        throw new RuntimeException("CRITICAL ERROR: Graph referenced Task ID " + task
                                + " but it is missing from the Registry!");
                    }
                    // System.out.println("Khan's Executing - " + task);
                    currentLayerTasks.add(workerTask);
                    taskExecuted++;
                    for (int child : adjList.get(task)) {
                        indegree[child]--;
                        if (indegree[child] == 0) {
                            queue.add(child);
                        }
                    }
                }
                int[] executionTimes = dispatcher.dispatchParallelBatch(currentLayerTasks);
                int[] spikes = analyzer.analyzeBottlenecks(executionTimes);
                for (int spike : spikes) {
                    if (spike == 1) {
                        System.out
                                .println(
                                        "[TELEMETRY ALERT] Cascading Failure Risk Detected in Layer! Scale up required.");
                    }
                }
                currentLevel++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dispatcher.shutdown();
        }
    }

    public static void main(String args[]) {
        int taskCount = 5;
        DAG engineGraph = new DAG(taskCount);
        
        // A "Wide" Graph: Tasks 0, 1, 2, and 3 have NO prerequisites. 
        engineGraph.addDependency(0, 4);
        engineGraph.addDependency(1, 4);
        engineGraph.addDependency(2, 4);
        engineGraph.addDependency(3, 4);

        Map<Integer, EngineTask> registry = new HashMap<>();

        System.out.println("[BOOT] Starting AstraEngine Work-Stealing Integration Test...");
        
        // LAYER 0 (Parallel Execution) - Casting null to String resolves constructor ambiguity
        registry.put(0, new EngineTask(0, "Gather_Qubits", (String) null, "DATA_0", "Write a 1-sentence summary of a Qubit."));
        registry.put(1, new EngineTask(1, "Gather_Superposition", (String) null, "DATA_1", "Write a 1-sentence summary of Quantum Superposition."));
        registry.put(2, new EngineTask(2, "Gather_Entanglement", (String) null, "DATA_2", "Write a 1-sentence summary of Quantum Entanglement."));
        registry.put(3, new EngineTask(3, "Gather_Interference", (String) null, "DATA_3", "Write a 1-sentence summary of Quantum Interference."));
        
        // LAYER 1 (Aggregator) - THE FIX IN ACTION: Pulling 4 memory keys at once!
        registry.put(4, new EngineTask(4, "Aggregator", Arrays.asList("DATA_0", "DATA_1", "DATA_2", "DATA_3"), null, "Translate this text into Spanish: "));

        List<List<Integer>> adjList = engineGraph.getList();
        AstraEngine executor = new AstraEngine();
        executor.runWorkflow(taskCount, adjList, registry);
        System.out.println("[BOOT] Execution Complete.");
    }}
