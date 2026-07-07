package engine.core;

import engine.interfaces.ITelemetryAnalyzer;
import java.util.Stack;

public class TelemetryAnalyzer implements ITelemetryAnalyzer {
    public int[] analyzeBottlenecks(int[] executionTimes){
        int n = executionTimes.length;
        int[] result = new int[n];
        Stack<Integer> stack = new Stack<>();
        for(int i = 0; i< n; i++){
            while((!stack.isEmpty()) && executionTimes[stack.peek()] < executionTimes[i]){
                int poped = stack.pop();
                int distance = i - poped;
                result[poped] = distance;
            }
            stack.push(i);
        }
        return result;
    }
}
