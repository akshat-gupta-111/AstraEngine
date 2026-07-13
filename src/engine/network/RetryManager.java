package engine.network;

import java.util.concurrent.Callable;

import engine.interfaces.IRetryManager;
import engine.models.EngineTask;

public class RetryManager implements IRetryManager {

    @Override
    public String executeWithRetry(int maxRetries, Callable<String> networkCall) throws Exception{
        int backoffDelay = 1000;
        for(int attempt = 1; attempt <= maxRetries; attempt++){
            try{
                return networkCall.call();
            }
            catch(Exception e){
                if(attempt == maxRetries){
                    throw e;
                }
                System.out.println("[RETRY] Network failed. Retrying attempt " + (attempt + 1) + " in " + backoffDelay + "ms...");
                Thread.sleep(backoffDelay);
                backoffDelay *= 2;
            }
        }
        throw new IllegalStateException("Retry loop failed unexpectedly");
    }
}
