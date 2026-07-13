# AstraEngine

AstraEngine is a high-performance, multithreaded DAG orchestration engine built in pure Java.

It is designed to execute dependent AI workflows with predictable ordering, parallel layer execution, custom work stealing, and lightweight in-memory caching.

## Highlights

- Topological workflow execution with Kahn's algorithm.
- Custom work-stealing dispatcher for parallel task layers.
- LRU-style engine memory cache for workflow context.
- Built-from-scratch data structures for teaching and experimentation.
- Defensive shutdown behavior for cycle detection, retries, and task failures.

## Architecture

The engine is organized around a small set of core responsibilities:

| Area | Purpose |
| --- | --- |
| `engine.core` | Orchestration, dispatching, telemetry, recovery, and engine state |
| `engine.network` | LLM client abstractions and retry handling |
| `engine.cache` | In-memory caching for task context |
| `engine.models` | DAG and task models |
| `engine.security` | Workflow validation and cycle detection |
| `dsa.*` | Supporting data structure implementations used throughout the engine |

## Core Data Structures

- Linked lists: used for pointer-based storage, queues, and ledgers.
- Work-stealing deque: supports fast local execution and rear stealing by idle workers.
- Hash map: provides indexed lookup for workflow data and cached results.
- LRU cache: keeps frequently used context available with bounded memory usage.
- Heaps: support priority-driven scheduling and analysis.
- Graph adjacency lists: represent the DAG and task dependencies.

## Algorithms

- Kahn's algorithm for topological batching.
- Depth-first search for traversal and validation.
- Floyd's cycle detection for linked-structure safety.
- Dijkstra's algorithm for shortest-path style analysis.
- Sliding-window parsing in the tokenizer.
- Exponential backoff for retry logic.

## Concurrency Model

- Worker threads are managed directly with `Thread` and `Runnable`.
- Granular synchronization is used around queue operations.
- `CountDownLatch` coordinates layer completion before the next batch starts.
- Worker interruption is handled explicitly during shutdown.

## Fault Tolerance

- Cycle detection stops invalid workflows before execution.
- `try`/`catch`/`finally` blocks ensure dispatcher shutdown always runs.
- Pre-execution checks fail fast when workflow state is inconsistent.
- Queue stealing helps recover work after a worker becomes unavailable.

## OOP Design

- Encapsulation keeps low-level node and queue manipulation internal.
- Interfaces decouple the engine from concrete dispatchers and clients.
- Polymorphism allows different dispatchers to be swapped in without changing orchestration code.
- Composition keeps the engine assembled from focused services instead of deep inheritance.

## Running The Demo

The main entry point is `src/engine/core/AstraEngine.java`.

From the `src` directory:

```bash
javac engine/core/AstraEngine.java
java engine.core.AstraEngine
```

## Project Layout

```text
src/
  dsa/                # Data structure implementation used by the engine
  engine/
    cache/            # Memory cache
    core/             # Orchestration and runtime control
    exceptions/       # Custom exception types
    interfaces/       # Engine contracts
    models/           # DAG and task models
    network/          # LLM clients and retries
    security/         # Workflow validation
    tokenizer/        # Prompt/token processing
    utils/            # Shared helpers
```

## Notes

This repository mixes production-style orchestration code with educational data structure implementations, so the README emphasizes both the runtime engine and the supporting algorithms that power it.
