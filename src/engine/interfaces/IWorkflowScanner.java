package engine.interfaces;

import java.util.List;

public interface IWorkflowScanner {

    boolean cycleDetection(List<List<Integer>> list, int size);

    
} 
