package engine.core;

public class RollbackLedger {

    public static class Ledger{
        public static class Node{
            public String commandId;
            public Node next;
            public Node prev;
            public Node(String data){
                this.commandId = data;
                this.next = null;
                this.prev = null;
            } 
        }
        public int size;
        public Node head;
        public Node tail;
        public Ledger(){
            this.size = 0;
            this.head = null;
            this.tail = null;
        }

        public void display(){
            Node ptr = head;
            while(ptr != null){
                System.out.print(ptr.commandId + " -> ");
                ptr = ptr.next;
            }
            System.out.println("null");
        }

        public void recordTask(String data){
            Node newNode = new Node(data);
            if(head == null){
                head = newNode;
                tail = newNode;
            }else{
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
            size++;
        }

        public void undo(int steps){
            Node currentTask = tail;
            System.out.println("Current task : " + currentTask.commandId);
            System.out.println("Tracing back " + steps + " steps.");
            if(steps > size){
                steps = size;
            }
            while(steps > 0){
                currentTask = currentTask.prev;
                if(currentTask != null) currentTask.next = null;
                steps--;
            }
            tail = currentTask;
            if(currentTask == null) {
                head = null;
                System.out.println("All tasks rollbacked!");
            }else{
                System.out.println("Current task : " + tail.commandId);
            }
        }
    }
    public static void main(String args[]){
        Ledger execution = new Ledger();
        execution.recordTask("START");
        execution.recordTask("TASK1");
        execution.recordTask("TASK2");
        execution.recordTask("TASK3");
        execution.display();
        execution.undo(5);
        execution.display();
        //execution.display();
    }
}
