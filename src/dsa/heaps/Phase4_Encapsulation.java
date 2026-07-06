package dsa.heaps;

public class Phase4_Encapsulation {
    public static class AstraHashMap{
        private static class Node{
            private String key;
            private String value;
            private Node next;
            private Node(String key, String value){
                this.key = key;
                this.value = value;
            }
        }
        private Node[] slots;
        private int capacity;

        public AstraHashMap(int capacity){
            this.capacity = capacity;
            this.slots = new Node[capacity];
        }

        private int hash(String key){
            int index = 0;
            for(char x : key.toCharArray()){
                index += x;
            }
            return index % capacity;
        }

        public void put(String key, String value){
            int index = hash(key);
            Node node = new Node(key, value);
            if(slots[index] == null){
                slots[index] = node;
                return;
            }else{
                Node ptr = slots[index];
                while(ptr != null){
                    if(ptr.key.equals(key)){
                        ptr.value = value;
                        return;
                    }
                    if(ptr.next == null){
                        break;
                    }
                    ptr = ptr.next;
                }
                ptr.next = node;
            }
        }

        public String get(String key){
            int index = hash(key);
            if(slots[index] == null){
                return "Empty Slot!";
            }
            Node ptr = slots[index];
            while(ptr != null){
                if(ptr.key.equals(key)){
                    return ptr.value;
                }
            }
            return "key does not exist!";
        }
    }
    public static class AstraTaskScheduler{
        private int[] heap;
        private int size;
        private int capacity;

        public AstraTaskScheduler(int capacity){
            this.capacity = capacity;
            this.heap = new int[capacity];
            int size = 0;
        }

        private int parent(int i){ return (i - 1)/ 2; }
        private int right(int i){ return (2 * i) + 2; }
        private int left(int i){ return (2 * i) + 1; }

        private void swap(int index1, int index2){
            int temp = heap[index1];
            heap[index1] = heap[index2];
            heap[index2] = temp;
        }

        public void scheduleTask(int task){
            if(capacity == size){
                System.out.print("Full!");
                return;
            }
            heap[size] = task;
            int currentIndex = size;
            size++;
            
            while(currentIndex > 0 && (heap[parent(currentIndex)] < heap[currentIndex])){
                swap(parent(currentIndex), currentIndex);
                currentIndex = parent(currentIndex);
            }
        }

        public int dispatchTask(){
            if(size == 0){
                System.out.println("No task scheduled!");
                return -1;
            }
            int highestPriorityTask = heap[0];
            heap[0] = heap[size - 1];
            heap[size -1] = 0;
            size--;

            int currentIndex = 0;

            while(left(currentIndex) < size){
                int largerChildIndex = left(currentIndex); // suppose;
                if(right(currentIndex) < size && heap[right(currentIndex)] > heap[largerChildIndex]){
                    largerChildIndex = right(currentIndex);
                }
                if(heap[currentIndex] < heap[largerChildIndex]){ 
                    swap(currentIndex, largerChildIndex);
                    currentIndex = largerChildIndex;
                }else{
                    break;
                }
            }
            return highestPriorityTask;
        }
    }
    public static void main(String[] args) {
        System.out.println("--- TESTING ENCAPSULATED MAP ---");
        AstraHashMap memory = new AstraHashMap(10);
        memory.put("AGENT_STATE", "IDLE");
        System.out.println("State is: " + memory.get("AGENT_STATE"));

        System.out.println("\n--- TESTING ENCAPSULATED HEAP ---");
        AstraTaskScheduler scheduler = new AstraTaskScheduler(10);
        scheduler.scheduleTask(5);
        scheduler.scheduleTask(99); // Critical
        scheduler.scheduleTask(10);
        System.out.println("Dispatched: " + scheduler.dispatchTask()); // Should be 99!
        System.out.println("Dispatched: " + scheduler.dispatchTask());
    }
}
