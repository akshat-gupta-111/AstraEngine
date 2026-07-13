package engine.models;

import java.util.Arrays;
import java.util.List;

public class EngineTask {
    private int taskId;
    private String taskName;
    private List<String> inputCacheKey;
    private String outputCacheKey;
    private String prompt;

    public EngineTask(int taskId, String taskName, List<String> inputCacheKey, String outputCacheKey, String prompt) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.inputCacheKey = inputCacheKey;
        this.outputCacheKey = outputCacheKey;
        this.prompt = prompt;
    }

    // 2. Convenience Constructor: Accepts a single String key (For backward
    // compatibility)
    public EngineTask(int taskId, String taskName, String inputCacheKey, String outputCacheKey, String prompt) {
        this.taskId = taskId;
        this.taskName = taskName;
        // Convert the single string into a list for consistent processing later
        this.inputCacheKey = (inputCacheKey == null) ? null : Arrays.asList(inputCacheKey);
        this.outputCacheKey = outputCacheKey;
        this.prompt = prompt;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public List<String> getInputCacheKey() {
        return inputCacheKey;
    }

    public String getOutputCacheKey() {
        return outputCacheKey;
    }

    public String getPrompt() {
        return prompt;
    }
}
