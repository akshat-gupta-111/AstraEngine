package dsa.stacks_queues;

import java.util.Arrays;

public class Phase5_Advanced {
    // CONCEPT 1: The Monotonic Stack

    public static int[] calculateNextBottleneck(int[] spikes) {
        int n = spikes.length;
        int[] result = new int[n];

        int[] stack = new int[n];
        int top = -1;

        for (int i = 0; i < n; i++) {
            int cpuLoad = spikes[i];
            while (top != -1 && spikes[stack[top]] < cpuLoad) {
                int bottleneckIndex = stack[top];
                top--;

                result[bottleneckIndex] = i - bottleneckIndex;
            }
            top++;
            stack[top] = i;
        }

        return result;
    }

    // CONCEPT 2: The Double-Ended Queue (Deque)

    public static class WorkStealingDeque {
        public class Node {
            int commandId;
            Node prev;
            Node next;

            public Node(int data) {
                this.commandId = data;
                this.next = null;
                this.prev = null;
            }
        }

        public Node front;
        public Node rear;
        public int size;

        public WorkStealingDeque() {
            this.front = null;
            this.rear = null;
            this.size = 0;
        }

        public void addFront(int data){
            Node node = new Node(data);
            if(front == null){
                front = node;
                rear = node;
            }else{
                node.next = front;
                front.prev = node;
                front = node;
            }
            System.out.println("[DEQUE] Added to Front: " + data);
        }
        public void addLast(int data){
            Node node = new Node(data);
            if(rear == null){
                front = node;
                rear = node;
            }else{
                rear.next = node;
                node.prev = rear;
                rear = node;
            }
            System.out.println("[DEQUE] Added to Last: " + data);
        }

        public int removeFront() {
            int val = -1;
            if(front == null){
                System.out.print("Empty Deque!");
            }else{
                val = front.commandId;
                front = front.next;
                if(front == null){ 
                    rear = null;
                }else{
                    front.prev = null;
                }
            }
            System.out.println("[DEQUE] STOLEN from Front: " + val);
            return val;
        }
        public int removeLast() {
            int val = -1;
            if(rear == null){
                System.out.print("Empty Deque!");
            }else{
                val = rear.commandId;
                rear = rear.prev;
                if(rear == null) front = null;
                else rear.next = null;
            }
            System.out.println("[DEQUE] STOLEN from Rear: " + val);
            return val;
        }
        public void display(){
            Node ptr = front;
            while(ptr != null){
                System.out.print(ptr.commandId + " -> ");
                ptr = ptr.next;
            }
            System.out.println("null");
        }
    }
    public static void main(String[] args) {
        System.out.println("====== ASTRA ENGINE: MONOTONIC STACK ======");
        int[] dailyCpuLoads = {73, 74, 75, 71, 69, 72, 76, 73};
        System.out.println("CPU Loads: " + Arrays.toString(dailyCpuLoads));
        
        int[] bottleneckDistances = calculateNextBottleneck(dailyCpuLoads);
        System.out.println("Steps until worse bottleneck: " + Arrays.toString(bottleneckDistances));
        // Output reading: For 73, wait 1 step (to hit 74). For 75, wait 4 steps (to hit 76). For 71, wait 2 steps (to hit 72).


        System.out.println("\n====== ASTRA ENGINE: WORK STEALING DEQUE ======");
        WorkStealingDeque engineDeque = new WorkStealingDeque();
        
        // Thread A loads up tasks
        engineDeque.addFront(100);
        engineDeque.addFront(101);
        engineDeque.addFront(102);
        engineDeque.addFront(103);
        
        // Thread A processes its most recent task (LIFO behavior)
        engineDeque.removeFront(); // Pops 103
        
        // Thread B finishes early and STEALS from Thread A's queue (FIFO behavior)
        engineDeque.removeLast();  // Steals 101 from the very bottom!
        engineDeque.addLast(104);
        engineDeque.display();
    }
}
