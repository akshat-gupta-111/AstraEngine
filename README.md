# 🚀 AstraEngine

> **A High-Performance, Multithreaded DAG Orchestration Engine built in pure Java.**

AstraEngine is a custom-built distributed computing engine designed to execute complex, dependent AI workflows. Built entirely from scratch without reliance on standard library abstractions, it features **Kahn's Algorithm** for topological sorting, a **custom barrier-synchronized Work-Stealing thread pool**, and **dynamic LRU memory caching**.

---

## 📂 Project Structure

```text
src/
├── dsa/                       # The Laboratory: Algorithmic evolutions and data structures
│   ├── lists/                 # Brute-force to O(1) Doubly Linked Lists & Cycle Detection
│   ├── stacks_queues/         # Monotonic stacks and Work-Stealing Deques
│   ├── hashmaps_heaps/        # Collision-chained HashMaps and Array-based Heaps
│   └── graphs/                # Adjacency lists, Kahn's Topological Sort, and Dijkstra's
│
└── engine/                    # The Production Core
    ├── cache/                 # EngineMemoryCache (LRU Cache implementation)
    ├── core/                  # AstraEngine Orchestrator, WorkStealingDispatcher, KahnsExecutor
    ├── interfaces/            # Decoupled interface contracts
    ├── models/                # EngineTask and DAG memory models
    ├── network/               # GeminiLLMClient and RetryManager
    └── tokenizer/             # SystemTokenizer
```

---

## 🧠 The DSA Evolution



This engine was not built using out-of-the-box frameworks. Every component was engineered from the ground up, evolving from brute-force memory allocation to enterprise-grade, thread-safe structures.

1. Memory Mapping & Chaining (Linked Lists)

Brute-Force to O(1) Optimization: Transitioned from O(N) tail-chasing insertions (Phase2_BruteForce) to maintaining encapsulated Tail Pointers for O(1) state ledger appends (Phase4_StateLedger).

Bidirectional State (Doubly Linked Lists): Engineered prev pointers to create instantaneous Undo/Redo rollback capabilities.

Floyd’s Cycle Detection: Implemented the Tortoise & Hare algorithm to mathematically guarantee no circular memory leaks exist in the engine's execution paths.

2. Execution Routing (Stacks & Queues)

Sub-Routine Stacks & Task Queues: Built native LIFO and FIFO execution pipelines entirely out of raw memory nodes to avoid java.util overhead.

The Monotonic Stack (Critical Analysis): Implemented a purely analytical decreasing stack to calculate CPU telemetry bottlenecks (Next Greater Element). Instead of using an O(N²) nested loop to check future CPU loads, this structure pushes un-triggered array indices onto a stack, instantly popping them the moment a higher CPU load is encountered. This mathematical manipulation maps sequential telemetry spikes in strict O(N) time.

Work-Stealing Deque: Upgraded standard queues into Double-Ended Queues using DoublyNodes. This exact structure powers the multithreading engine, allowing native LIFO execution from the front and lock-free FIFO theft from the rear.

3. Global Memory & Priority (HashMaps & Heaps)

Collision-Chained HashMaps: Built a custom String-to-Index hashing function utilizing ASCII character summation and modulo (% CAPACITY) routing. Engineered Linked-List chaining to gracefully handle data collisions inside flat arrays.

Array-Based Tree Mathematics (Heaps): Mastered the illusion of Trees inside flat arrays using (2*i)+1 and (i-1)/2 parent-child routing. Implemented bubble up and sink down logic for instant O(1) Max-Heap priority task extraction.

The "Top-K" Pattern (Min-Heaps): Utilized inverted Min-Heaps to dynamically track the absolute slowest system bottlenecks out of massive streams of execution data.

The LRU Cache: Culminated in Phase5_Advanced by fusing a HashMap with a Doubly Linked List. Achieved O(1) Read/Write access while automatically severing and evicting the Least Recently Used memory nodes to protect heap capacity.

4. Dependency Graphing (DAGs)

Adjacency Lists: Mapped complex workflows using Arrays of Linked Lists to represent directed execution edges.

DFS Topological Sort: Utilized Deep Recursion and Stacks to push "dead-end" nodes to the bottom, generating mathematically perfect chronological execution plans.

Dijkstra’s Algorithm: Fused Graphs with Min-Heaps to calculate the lowest-latency execution paths across simulated weighted nodes, bypassing shorter (but slower) structural paths.

Deadlock Detection (Security Scanner): Engineered dual-state tracking (visited and inRecursionStack) to instantly detect cyclic dependencies and halt the engine before an infinite loop triggers.

⚙️ Core Engine Algorithms

Kahn's Algorithm (BFS Topological Sort): The heartbeat of AstraEngine. It tracks in-degree dependencies, sweeping zero-dependency tasks into parallel layers, executing them, and dynamically decrementing children to unblock the next wave of parallelism.

Sliding Window Tokenization: Parses and chunks LLM payloads dynamically by evaluating spaces and newlines, acting as a pre-network safety valve against payload overflows.

Exponential Backoff: Network retry logic that aggressively scales delay intervals to survive HTTP 429 (Resource Exhausted) external API limits.

🧵 Concurrency & Backend Architecture

Custom OS Thread Pooling: Evolved beyond Java's ExecutorService to manually instantiate, manage, and ignite physical OS threads via the Thread class.

The Work-Stealing Algorithm: A dynamic load-balancing paradigm where starved CPU threads actively hunt and steal tasks from the rear queues of overloaded threads to maximize hardware utilization.

Fine-Grained Locking: Strategically scoped synchronized blocks on granular memory operations (removeFront, addRear) to prevent race conditions while completely avoiding global method deadlocks.

Barrier Synchronization (CountDownLatch): Enforces strict chronological layer execution. The Orchestrator halts at a barrier, waiting for all independent threads in Layer N to decrement the latch before Layer N+1 is unleashed.

Thread Yielding: Mitigated CPU meltdown during infinite polling (while(true)) by injecting Thread.sleep(10) micro-yields for idle worker threads.

🛡️ Exception Handling & Fault Tolerance

The try-catch-finally Deadlock Guarantee: In custom threading without Future wrappers, unhandled runtime exceptions instantly freeze the system. By injecting layerBarrier.countDown() inside a structural finally block, the engine mathematically guarantees the barrier is lowered even if the network API violently crashes.

Fail-Fast Safety Valves: Actively evaluating API payload limits before initiating network execution, throwing proactive RuntimeException triggers to halt rogue tasks instantly.

Self-Healing Distributed Queues: The Work-Stealing architecture proved inherently fault-tolerant; surviving threads dynamically detected crashed peers, stole their abandoned tasks, and completed the execution layer without human intervention.

🧩 Object-Oriented System Design

Encapsulation: Shielded inner Node and Edge manipulation logic, exposing only high-level abstraction APIs (e.g., scheduleTask, append).

Method Overloading: Maintained backward compatibility by designing polymorphic constructors, allowing the engine to seamlessly convert legacy single-String caches into modern multi-String aggregation Lists.

Composition over Inheritance: Structured the Master Orchestrator as a composite object that has-a RetryManager, has-a MemoryCache, and has-a Dispatcher, maximizing modular flexibility.

Interface Abstraction: Decoupled the Orchestrator from the Hardware via ITaskDispatcher and ILLMClient contracts, allowing the engine to hot-swap from single-threaded, to ExecutorService, to custom Work-Stealing architectures without rewriting core logic.