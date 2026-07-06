 package dsa.graphs;

public class Phase2_BruteForceGraph {
    public static class Node{
        public int targetTask;
        public Node next;
        public Node(int data){
            this.targetTask = data;
            this.next = null;
        }
    }
    static int totalTask = 5;
    static Node[] adjList = new Node[totalTask];

    public static void addDependency(int from , int to){
        Node node = new Node(to);
        if(adjList[from] == null){
            adjList[from] = node;
        }else{
            node.next = adjList[from];
            adjList[from]  = node;
        }
    }

    public static void printWorkFlow(){
        for(int i = 0; i< totalTask; i++){
            System.out.print("Task ID " + i + " Invokes : ");
            Node curr = adjList[i];
            if(curr == null){
                System.out.println(" [END of Pipeline] ");
            }

            while(curr != null){
                System.out.print("Task["+curr.targetTask+"] ");
                if(curr.next != null) System.out.print("-> ");
                curr = curr.next;
            }
            System.out.println();
        }
    }
    
    
    public static void main(String args[]){
        addDependency(0, 1);
        addDependency(0, 2);
        addDependency(0, 0);
        addDependency(1, 0);
        addDependency(1, 2);
        addDependency(2, 3);
        addDependency(3, 4);
        addDependency(4, 0);

        printWorkFlow();
    }
}