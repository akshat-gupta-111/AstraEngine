package dsa.graphs;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Queue;

public class Phase4_EncapsulatedGraph {
    public static class WorkFlowRouter {
        private int totalTask;
        private LinkedList<Integer>[] slots;

        @SuppressWarnings("unchecked")
        public WorkFlowRouter(int totalTask) {
            this.totalTask = totalTask;
            this.slots = new LinkedList[totalTask];

            for (int i = 0; i < totalTask; i++) {
                slots[i] = new LinkedList<>();
            }
        }

        public void addDependency(int from, int to) {
            slots[from].add(to);
        }
        // DFS
        public void generateExecutionPlan() {
            Stack<Integer> execution = new Stack<>();
            boolean[] visited = new boolean[totalTask];
            for (int i = 0; i < totalTask; i++) {
                if (!visited[i]) {
                    dfshelper(i, visited, execution);
                }
            }

            System.out.println("DFS done-----");
            while (!execution.isEmpty()) {
                System.out.print(execution.pop() + " -> ");
            }
            System.out.println();
        }

        public void dfshelper(int current, boolean[] visited, Stack<Integer> stack) {
            visited[current] = true;

            for (Integer child : slots[current]) {
                if (!visited[child]) {
                    dfshelper(child, visited, stack);
                }
            }

            stack.push(current);
        }


        // BFS - Khan's Algo

        public void parallelExecution(){
            Queue<Integer> queue = new LinkedList<>();
            int[] indegree = new int[totalTask];
            int currentLevel = 0; //no use just to print
            int taskExecuted = 0;
            for(int curr = 0; curr < totalTask; curr++){
                for(Integer child : slots[curr]){
                    indegree[child]++;
                }
            }
            for(int i = 0; i< totalTask; i++){
                if(indegree[i] == 0){
                    queue.add(i);
                }
            }

            while(!queue.isEmpty()){
                int size = queue.size();
                System.out.print("Current level["+currentLevel+"] : ");

                for(int i = 0 ; i< size ; i++){
                    int curr = queue.poll() ;
                    System.out.print(curr+ " ");
                    taskExecuted++;
                    for(Integer child : slots[curr]){
                        indegree[child]--;
                        if(indegree[child]==0){
                            queue.add(child);
                        }
                    }
                }
                System.out.println();
                currentLevel++;
            }
            if(taskExecuted != totalTask){
                System.out.println("Deadlock");
            }
        }

        public void printWorkFlow() {
            for(int i = 0 ; i< totalTask; i++){
                System.out.print("Task : "+ i+ " -> ");
                for(Integer curr : slots[i]){
                    System.out.print("Task["+curr+"] -");
                }
                System.out.println();
            }
        }

    }

    public static void main(String[] args) {
        System.out.println("====== INITIALIZING WORKFLOW ROUTER ======");
        WorkFlowRouter engineGraph = new WorkFlowRouter(6);

        // Building a complex DAG:
        // Task 5 & 4 are independent starting points.
        engineGraph.addDependency(5, 2);
        engineGraph.addDependency(5, 0);
        engineGraph.addDependency(4, 0);
        engineGraph.addDependency(4, 1);
        engineGraph.addDependency(2, 3);
        engineGraph.addDependency(3, 5);
        engineGraph.addDependency(3, 1);
        engineGraph.printWorkFlow();
        /*
         * Visualizing the rules we just wrote:
         * Task 0 must wait for 5 and 4.
         * Task 2 must wait for 5.
         * Task 3 must wait for 2.
         * Task 1 must wait for 4 and 3.
         */

        // Let the engine calculate the only safe execution order
        engineGraph.generateExecutionPlan();
        System.out.println("BFS -------------");
        engineGraph.parallelExecution();
        // Expected Output: 5 -> 4 -> 2 -> 3 -> 1 -> 0 (or similar valid topological
        // order)
    }
}
