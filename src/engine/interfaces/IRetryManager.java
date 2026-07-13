package engine.interfaces;
import engine.models.EngineTask;
import java.util.concurrent.Callable;
public interface IRetryManager {
    String executeWithRetry(int maxRetries, Callable<String> networkCall) throws Exception;
}
