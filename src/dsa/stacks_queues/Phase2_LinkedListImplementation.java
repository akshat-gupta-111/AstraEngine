package dsa.stacks_queues;

public class Phase2_LinkedListImplementation {
    public static class Node {
        public int commandId;
        public Node next;

        public Node(int data) {
            this.commandId = data;
            this.next = null;
        }
    }

    public static class LinkedStack {
        private Node top;
        private int size;

        private LinkedStack() {
            this.size = 0;
            this.top = null;
        }

        public void push(int data) {
            Node newNode = new Node(data);

            newNode.next = top;
            top = newNode;

            size++;
            System.out.println("[STACK] Pushed Sub-Task: " + data);
        }

        public int pop() {
            int val = -1;
            if (top == null) {
                System.out.println("Empty Stack!");
            } else {
                val = top.commandId;
                top = top.next;
                size--;
            }
            System.out.println("[STACK] Popped Sub-Task: " + val);
            return val;
        }

        public void display() {
            Node ptr = top;
            System.out.print("Top [");
            while (ptr != null) {
                System.out.print(ptr.commandId + " <- ");
                ptr = ptr.next;
            }
            System.out.println("] Bottom");
        }
    }

    public static class LinkedQueue {
        public Node front;
        public Node rear;
        public int size;

        public LinkedQueue() {
            this.front = null;
            this.rear = null;
            this.size = 0;
        }

        public void enqueue(int data) {
            Node newNode = new Node(data);
            if (rear == null) {
                rear = newNode;
                front = rear;
            } else {
                rear.next = newNode;
                rear = newNode;
            }
            size++;
            System.out.println("[QUEUE] Enqueued Task: " + data);
        }

        public int dequeue() {
            int val = -1;
            if (front == null) {
                System.out.println("Empty Queue");
            } else {
                val = front.commandId;
                front = front.next;
            }
            if (front == null)
                rear = null;
            return val;
        }

        public void display() {
            Node current = front;
            System.out.print("Queue State (Front to Rear): ");
            while (current != null) {
                System.out.print("[" + current.commandId + "] -> ");
                current = current.next;
            }
            System.out.println("null");
        }
    }

    public static void main(String[] args) {
        System.out.println("====== ASTRA ENGINE: STACK DEMO ======");
        LinkedStack subRoutineStack = new LinkedStack();
        subRoutineStack.push(101); // Main Task
        subRoutineStack.push(102); // Sub-Task A
        subRoutineStack.push(103); // Sub-Task A.1
        subRoutineStack.display();

        // Execute the most recently added task first
        subRoutineStack.pop();
        subRoutineStack.display();

        System.out.println("\n====== ASTRA ENGINE: QUEUE DEMO ======");
        LinkedQueue taskDispatcher = new LinkedQueue();
        taskDispatcher.enqueue(500); // User Request 1
        taskDispatcher.enqueue(501); // User Request 2
        taskDispatcher.enqueue(502); // User Request 3
        taskDispatcher.display();

        // Execute the oldest task first
        taskDispatcher.dequeue();
        taskDispatcher.display();
    }
}
