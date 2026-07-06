package dsa.graphs;

import java.util.*;

public class Phase5_AdvancedGraphs{
     // CONCEPT 1: DIJKSTRA'S SHORTEST PATH (Graph + Heap)
    public static class NetworkRouter{
        public class Edge{
            public int targetNode;
            public int weight;
            public Edge(int targetNode, int weight){
                this.targetNode = targetNode;
                this.weight = weight;
            }
        }

        public List<List<Edge>> adjList;
        public int total;

        public NetworkRouter(int total){
            this.total = total;
            adjList = new ArrayList<>();
            for(int i = 0; i< total ;i++){
                adjList.add(new ArrayList<>());
            }
        }
        public void addRoute(int from,int to , int weight){
            adjList.get(from).add(new Edge(to, weight));
        }

        public void algorithm(int start){
            PriorityQueue<Edge> minheap = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
            int[] fastestDistance = new int[total];
            Arrays.fill(fastestDistance, Integer.MAX_VALUE);
            fastestDistance[start] = 0;
            minheap.add(new Edge(start, 0));

            while(!minheap.isEmpty()){
                Edge currentEdge = minheap.poll();
                int currentNode = currentEdge.targetNode;
                int currentWeight = currentEdge.weight;

                if(currentWeight > fastestDistance[currentNode]) continue;

                for(Edge neighbour : adjList.get(currentNode)){
                    int newWeight = currentWeight + neighbour.weight;

                    if(newWeight < fastestDistance[neighbour.targetNode]){
                        fastestDistance[neighbour.targetNode] = newWeight;
                        minheap.add(new Edge(neighbour.targetNode, newWeight));
                    }
                }
            }
            System.out.println("The distance of nodes from the : "+start+" : ");
            for(int i = 0; i< total; i++){
                System.out.println("TO ["+i+"] is : "+ fastestDistance[i]);
            }
        }
    }
    public static class SecurityScanner{
        private List<List<Integer>> adjList;
        private int total;

        public SecurityScanner(int total){
            this.total = total;
            adjList = new ArrayList<>();
            for(int i = 0; i < total; i++){
                adjList.add(new ArrayList<>());
            }
        }

        public void addDependency(int from , int to){
            adjList.get(from).add(to);
        }

        public boolean containsDeadlock(){
            boolean[] visited = new boolean[total];
            boolean[] inRecursion = new boolean[total];

            for(int i = 0; i< total ; i++){
                if(dfsDeadlockDetection(i, visited, inRecursion)){
                    return true;
                }
            }
            return false;
        }
        public boolean dfsDeadlockDetection(int current , boolean[] visited, boolean[] inRecursion){
            if(inRecursion[current]) return true;
            
            if(visited[current]) return false;
            

            visited[current] = true;
            inRecursion[current] = true;

            for(int neighbour : adjList.get(current)){
                if(dfsDeadlockDetection(neighbour, visited, inRecursion)){
                    return true;
                }
            }

            inRecursion[current] = false;
            return false;
        }
    }

    public static void main(String[] args){
        SecurityScanner sc = new SecurityScanner(4);
        sc.addDependency(1, 0);
        sc.addDependency(0, 2);
        sc.addDependency(0, 3);
        sc.addDependency(2, 3);
        
        System.out.println("Cycle : "+sc.containsDeadlock());

        NetworkRouter nc = new NetworkRouter(4);
        nc.addRoute(0, 1, 1);
        nc.addRoute(0, 2, 0);
        nc.addRoute(1, 3, 1);
        nc.addRoute(2, 3, 0);

        nc.algorithm(0);
    }
}