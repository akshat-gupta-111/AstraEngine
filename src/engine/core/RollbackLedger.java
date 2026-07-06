package engine.core;

public class RollbackLedger {

    public static class Ledger {
        public static class Node {
            public String commandId;
            public Node next;
            public Node prev;

            public Node(String data) {
                this.commandId = data;
                this.next = null;
                this.prev = null;
            }
        }

        public int size;
        public Node head;
        public Node tail;

        public Ledger() {
            this.size = 0;
            this.head = null;
            this.tail = null;
        }

        public void display() {
            Node ptr = head;
            while (ptr != null) {
                System.out.print(ptr.commandId + " -> ");
                ptr = ptr.next;
            }
            System.out.println("null");
        }

        public void recordTask(String data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
            size++;
        }

        public void undo(int steps) {
            if (tail == null || size == 0) {
                System.out.println("[WARNING] Ledger is already empty. Nothing to undo.");
                return; // Prevent NullPointerException
            }

            System.out.println("Current task : " + tail.commandId);

            if (steps > size) {
                steps = size; // Cap the steps to our actual size
            }

            System.out.println("Tracing back " + steps + " steps.");

            // Decrease the size tracker accurately
            size -= steps;

            Node currentTask = tail;
            while (steps > 0) {
                currentTask = currentTask.prev;
                if (currentTask != null) {
                    currentTask.next = null; // Sever the forward pointer for GC
                }
                steps--;
            }

            tail = currentTask; // Update the tail to the new end

            if (tail == null) {
                head = null; // If tail is null, the whole list is empty
                System.out.println("All tasks rolled back! Engine is in starting state.");
            } else {
                System.out.println("Current task is now : " + tail.commandId);
            }
        }
    }

    public static void main(String args[]) {
        Ledger execution = new Ledger();
        execution.recordTask("START");
        execution.recordTask("TASK1");
        execution.recordTask("TASK2");
        execution.recordTask("TASK3");
        execution.display();
        execution.undo(5);
        execution.display();
        // execution.display();
    }
}
