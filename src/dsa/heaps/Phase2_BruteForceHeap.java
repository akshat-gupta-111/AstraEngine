package dsa.heaps;

public class Phase2_BruteForceHeap {
    static int capacity = 10;
    static int[] priorityQueue = new int[capacity];

    static int size = 0;

    static int getParentIndex(int i){ return (i - 1)/2; }
    static int getLeftChildIndex(int i){ return (2 * i) + 1; }
    static int getRightChildIndex(int i){ return (2 * i) + 2;}

    static void swap(int index1,int index2){
        int temp = priorityQueue[index1];
        priorityQueue[index1] = priorityQueue[index2];
        priorityQueue[index2] = temp;
    }

    static void displayHeap() {
        System.out.print("Engine Priority Array: [ ");
        for (int i = 0; i < size; i++) {
            System.out.print(priorityQueue[i] + " ");
        }
        System.out.println("]");
    }
    static void insertTask(int priorityWeight){
        if(size == capacity){
            System.out.println("Queue is full");
            return;
        }
        int currentIndex = size;
        priorityQueue[currentIndex] = priorityWeight;
        size++;

        System.out.println("Inserted Priority " + priorityWeight + " at the bottom. Bubbling up...");

        while(currentIndex > 0){
            int parentIndex = getParentIndex(currentIndex);

            if(priorityQueue[currentIndex] > priorityQueue[parentIndex]){
                swap(currentIndex, parentIndex);
                currentIndex = parentIndex;
            }else{
                break;
            }
        }
    }

    static int dispatchHighestPriority(){
        if(size == 0){
            return -1;
        }
        int highestPriorityTask = priorityQueue[0];
        System.out.println("\n[DISPATCHING] Executing Task with Priority: " + highestPriorityTask);

        priorityQueue[0] = priorityQueue[size - 1];
        priorityQueue[size - 1] = 0;
        size--;

        int currentIndex = 0;

        while(getLeftChildIndex(currentIndex) < size){
            int leftChildIndex = getLeftChildIndex(currentIndex);
            int rightChildIndex = getRightChildIndex(currentIndex);

            int largerChildIndex = leftChildIndex; //assume

            if(rightChildIndex < size && priorityQueue[rightChildIndex] > priorityQueue[leftChildIndex]){
                largerChildIndex = rightChildIndex;
            }
            if(priorityQueue[currentIndex] < priorityQueue[largerChildIndex]){
                swap(currentIndex, largerChildIndex);
                currentIndex = largerChildIndex;
            }else{
                break;
            }
        }
        return highestPriorityTask;
    }
    public static void main(String[] args) {
        System.out.println("====== INITIALIZING ASTRA ENGINE HEAP ======");
        
        // We insert random priority tasks.
        // Notice how the array automatically reorganizes so the highest number is at index 0.
        insertTask(10); // Standard Task
        insertTask(5);  // Low Priority
        insertTask(20); // High Priority
        insertTask(3);  // Background Task
        insertTask(50); // CRITICAL EMERGENCY TASK

        System.out.println("\n--- HEAP STATE AFTER INSERTIONS ---");
        displayHeap(); // The array is NOT perfectly sorted! But 50 is at index 0.

        // Dispatching takes the top priority (50), and reorganizes automatically.
        dispatchHighestPriority();
        displayHeap(); // Now 20 takes the crown at index 0!

        dispatchHighestPriority();
        displayHeap(); // Now 10 is at the top.
    }
}
