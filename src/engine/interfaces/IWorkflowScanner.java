package engine.interfaces;

import java.util.List;
import engine.models.EngineTask;
public interface IWorkflowScanner {

    boolean cycleDetection(List<List<EngineTask>> list, int size);

    
} 
