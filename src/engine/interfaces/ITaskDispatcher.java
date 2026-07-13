package engine.interfaces;
import java.util.List;

import engine.models.EngineTask;

public interface ITaskDispatcher {
    int[] dispatchParallelBatch(List<EngineTask> layer);
    void shutdown();
}
