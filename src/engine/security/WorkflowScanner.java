package engine.security;
import java.util.List;

import engine.exceptions.CyclicDeadlockException;
import engine.interfaces.IWorkflowScanner;
import engine.models.EngineTask;

public class WorkflowScanner implements IWorkflowScanner {
    @Override
    public boolean cycleDetection(List<List<EngineTask>> list, int size){
        boolean[] visited = new boolean[size];
        boolean[] inActivePath = new boolean[size];
        for(int i = 0 ; i< size; i++ ){
            if(dfsCycleHunt(i, visited, inActivePath, list)){
                throw new CyclicDeadlockException("AstraEngine Pre-Flight Halt: Cyclic dependency detected! Workflow rejected.");
            }
        }
        return false;
    }

    private boolean dfsCycleHunt(int current , boolean[] visited, boolean[] inActivePath,List<List<EngineTask>> list){
        if(inActivePath[current]) return true;
        if(visited[current]) return false;

        inActivePath[current] = true;
        visited[current] = true;

        for(EngineTask  neighbours : list.get(current)){
            if(dfsCycleHunt(neighbours.task, visited, inActivePath, list)){
                return true;
            }
        }

        inActivePath[current] = false;
        return false;
    }
}
