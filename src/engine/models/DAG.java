package engine.models;

import java.util.*;

public class DAG {
    private int totalTask;
    private LinkedList<Integer>[] slots;

    @SuppressWarnings("unchecked")
    public DAG(int totalTask) {
        this.totalTask = totalTask;
        this.slots = new LinkedList[totalTask];

        for (int i = 0; i < totalTask; i++) {
            slots[i] = new LinkedList<>();
        }
    }

    public void addDependency(int from, int to) {
        slots[from].add(to);
    }

    public void printWorkFlow() {
        for (int i = 0; i < totalTask; i++) {
            System.out.print("Task : " + i + " -> ");
            for (Integer curr : slots[i]) {
                System.out.print("Task[" + curr + "] -");
            }
            System.out.println();
        }
    }
    public List<List<Integer>> getList(){
        List<List<Integer>> result = new ArrayList<>();
        for(int i = 0; i< totalTask; i++){
            result.add(new ArrayList<>(slots[i]));
        }
        return result;
    }
}
