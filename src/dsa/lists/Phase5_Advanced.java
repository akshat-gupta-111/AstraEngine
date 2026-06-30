package dsa.lists;

public class Phase5_Advanced {
    public static class doublyNode{
        public int commandId;
        public doublyNode next;
        public doublyNode prev;

        public doublyNode(int data){
            this.commandId = data;
            this.next=null;
            this.prev=null;
        }
    }
    public static void demoUndoRedo(){
            System.out.println("--- 1. DOUBLY LINKED LIST (Undo/Redo System) ---");

            doublyNode head = new doublyNode(101);

            doublyNode node2 = new doublyNode(102);

            head.next = node2;
            node2.prev = head;

            doublyNode tail = new doublyNode(103);
            tail.prev = node2;
            node2.next = tail;


            System.out.println("Execution History Created.");

            doublyNode currentExecution = tail;

            System.out.println("Current State: " + currentExecution.commandId);

        // TRIGGER UNDO: Step backward instantly using the 'prev' pointer
        System.out.println("\n[SYSTEM FAILURE] Rolling back state...");
        currentExecution = currentExecution.prev; 
        System.out.println("Rolled back to: " + currentExecution.commandId);

        // TRIGGER REDO: Step forward instantly
        System.out.println("\n[SYSTEM RECOVERED] Re-applying state...");
        currentExecution = currentExecution.next;
        System.out.println("Redone to: " + currentExecution.commandId);
    }

    public static boolean hasInfiniteLoop(doublyNode head){
        System.out.println("Floyd Cycle detection algorithm!");
        if(head == null){
            return false;
        }
        doublyNode slow = head;
        doublyNode fast = head;
        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
            if(slow == fast){
                return true;
            }
        }
        return false;
    }
    public static void demoCycleDetection(){
        doublyNode n1 = new doublyNode(1);
        doublyNode n2 = new doublyNode(2);
        doublyNode n3 = new doublyNode(3);

        n1.next = n2;
        n2.prev = n1;
        n2.next = n3;
        n3.prev = n2;
        n3.next = n1;
        n1.prev = n3;

        System.out.println("Checking standard linear list for cycles: " + hasInfiniteLoop(n1)); // Expected: false

        // INJECT A VIRUS: Make the end of the list point back to the middle!
        System.out.println("[WARNING] Injecting circular reference (n3 -> n1)...");
        n3.next = null; 
        
        System.out.println("Checking corrupted list for cycles: " + hasInfiniteLoop(n1)); // Expected: true
    }

    public static void main(String[] args){
        demoUndoRedo();
        demoCycleDetection();
    }
}

