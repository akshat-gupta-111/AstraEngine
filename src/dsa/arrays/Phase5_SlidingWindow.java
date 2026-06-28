package dsa.arrays;

public class Phase5_SlidingWindow {
    public static int detectPeakLoad(int[] loads, int windowSize){
        if(loads.length < windowSize) return -1;
        int currentLoad = 0;
        int maxLoad = 0;
        for(int i = 0 ; i< windowSize; i++){
            currentLoad += loads[i];
        }
        maxLoad = currentLoad;
        for(int i = windowSize; i < loads.length ; i++){
            currentLoad = currentLoad - loads[i - windowSize] + loads[i];
            if(currentLoad > maxLoad){
                maxLoad = currentLoad;
            }
        }
        return maxLoad;
    }
    public static void main(String[] args) {
        // Mock load metrics (e.g., threads used per millisecond)
        int[] systemLoads = {10, 50, 30, 20, 60, 40, 10}; 
        int window = 3;
        
        int peak = detectPeakLoad(systemLoads, window);
        System.out.println("Maximum sustained load over " + window + "ms window: " + peak);
    }
}
