package engine.security;
import java.util.List;

import engine.exceptions.CyclicDeadlockException;
import engine.interfaces.IWorkflowScanner;


public class WorkflowScanner implements IWorkflowScanner {
    
    public boolean cycleDetection(List<List<Integer>> list, int size){
        boolean[] visited = new boolean[size];
        boolean[] inActivePath = new boolean[size];
        for(int i = 0 ; i< size; i++ ){
            if(dfsCycleHunt(i, visited, inActivePath, list)){
                throw new CyclicDeadlockException("AstraEngine Pre-Flight Halt: Cyclic dependency detected! Workflow rejected.");
            }
        }
        return false;
    }

    private boolean dfsCycleHunt(int current , boolean[] visited, boolean[] inActivePath,List<List<Integer>> list){
        if(inActivePath[current]) return true;
        if(visited[current]) return false;

        inActivePath[current] = true;
        visited[current] = true;

        for(int  neighbour : list.get(current)){
            if(dfsCycleHunt(neighbour, visited, inActivePath, list)){
                return true;
            }
        }

        inActivePath[current] = false;
        return false;
    }
}
