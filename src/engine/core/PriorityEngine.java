package engine.core;

public class PriorityEngine {
    public static class CustomHeap{
        private class EngineTask{
            private String task;
            private int weight;
            public EngineTask(String task, int weight){
                this.task = task;
                this.weight = weight;
            }
        }
        private int size;
        private int capacity;
        private EngineTask[] slots;

        public CustomHeap(int capacity){
            this.size =0;
            this.capacity = capacity;
            this.slots = new EngineTask[capacity];
        }
        private int parent(int i) {return (i - 1) / 2;}
        private int left(int i) { return (i * 2) + 1;}
        private int right(int i) { return (i * 2) + 2;}

        private void swap(int index1, int index2){
            EngineTask temp = slots[index1];
            slots[index1] = slots[index2];
            slots[index2] = temp; 
        }

        public void schedule(String task, int weight){
            System.out.println("Scheduling : " + task);
            if(size == capacity){
                System.out.println("Error - Full");
                return;
            }
            EngineTask engineTask = new EngineTask(task, weight);
            slots[size] = engineTask;
            int current = size;
            size++;
            while(current > 0){
                if(slots[current].weight > slots[parent(current)].weight){
                    swap(current, parent(current));
                    current = parent(current);
                }else{
                    return;
                }
            }
        }

        public String dispatch(){
            if(size == 0){
                System.out.println("Empty!");
                return "Empty!";
            }
            String highestPriorityTask = slots[0].task;
            slots[0] = slots[size - 1];
            slots[size - 1] = new EngineTask("Empty", -1); 
            size--;

            int current = 0;
            while(left(current) < size){
                int larger = left(current); //suppose
                if(right(current) < size && (slots[right(current)].weight > slots[left(current)].weight)){
                    larger = right(current);
                }
                if(slots[current].weight < slots[larger].weight){
                    swap(current, larger);
                    current = larger;
                }else{
                    break;
                }
            }
            System.out.println("Executing - " + highestPriorityTask);
            return highestPriorityTask;
        }
        public void display(){
            for(int i = 0; i< size; i++){
                System.out.print(slots[i].task + " - ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args){
        CustomHeap maxHeap = new CustomHeap(5);
        maxHeap.display();
        maxHeap.dispatch();
        maxHeap.schedule("TASK1", 1);
        maxHeap.display();
        maxHeap.schedule("TASK50", 50);
        maxHeap.display();
        maxHeap.schedule("TASK2", 2);
        maxHeap.display();
        maxHeap.schedule("TASK100", 100);
        maxHeap.display();
        maxHeap.schedule("TASK999", 999);
        maxHeap.display();
        maxHeap.schedule("TASK0", 0);
        maxHeap.dispatch();
        maxHeap.dispatch();
        maxHeap.dispatch();
        maxHeap.dispatch();
        maxHeap.dispatch();
    }
}
