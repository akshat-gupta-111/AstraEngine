package engine.tokenizer;

import java.util.Scanner;

public class SystemTokenizer {
    public static class DynamicCommandBuffer{
        private char[] buffer;
        private int size;
        private int capacity;

        public DynamicCommandBuffer(int initialCapacity){
            this.capacity = initialCapacity;
            this.size = 0;
            this.buffer = new char[capacity];
        }

        // Create

        public void add(char commandId){
            if(size == capacity){
                grow();
            }
            buffer[size] = commandId;
            size++;
        }

        public void insert(int index, char commandId){
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("System Error: Invalid buffer index access.");
            }
            if(size == capacity){
                grow();
            }
            for(int i = size -1; i>= index ; i--){
                buffer[i + 1] = buffer[i];
            }
            buffer[index] = commandId;
            size++;
        }
        //READ
        public int get(int index){
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("System Error: Invalid buffer index access.");
            }
            return buffer[index];
        }
        // UPDATE 
        public void update(int updateIndex, char updatedValue){
            if (updateIndex < 0 || updateIndex >= size) {
                throw new IndexOutOfBoundsException("System Error: Invalid buffer index access.");
            }
            buffer[updateIndex] = updatedValue;
        }

        // Delete 
        public void delete(int index){
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("System Error: Invalid buffer index access.");
            }
            for(int i = index; i< size - 1; i++){
                buffer[i] = buffer[i + 1];
            }
            buffer[size - 1] = '-';
            size--;
        }
        private void grow(){
            int newCapacity = capacity * 2;
            char[] newBuffer = new char[newCapacity];
            for(int i = 0; i< capacity; i++){
                newBuffer[i] = buffer[i];
            }
            System.out.println(">>> [BUFFER OVERFLOW ALERT]: Capacity doubled from " + capacity + " to " + newCapacity);
            this.buffer = newBuffer;
            this.capacity = newCapacity;
        }

        public int getSize(){
            return this.size;
        }
        public int getCapacity(){
            return this.capacity;
        }
        public void display() {
            System.out.print("Engine Active State: [ ");
            for (int i = 0; i < capacity; i++) {
                if (i < size) System.out.print(buffer[i] + " ");
                else System.out.print("__ ");
            }
            System.out.println("] | Size: " + size + "/" + capacity);
        }
    }
    
    public static int tokenizer(String s){
        char[] arr = s.toCharArray();
        DynamicCommandBuffer[] buffer = new DynamicCommandBuffer[200]; 
        int tokenCount = 0;
        int windowStart = 0;

        for(int windowEnd = 0; windowEnd <= arr.length; windowEnd++){
           
            if (windowEnd == arr.length || arr[windowEnd] == ' ' || arr[windowEnd] == '\n'){
                
              
                if (windowEnd > windowStart) { 
                    int capNeeded = windowEnd - windowStart;
                    DynamicCommandBuffer curr = new DynamicCommandBuffer(capNeeded);
                    for(int i = windowStart; i< windowEnd; i++){
                        curr.add(arr[i]);
                    }
                    buffer[tokenCount] = curr;
                    tokenCount++;
                }
                windowStart = windowEnd + 1;
            }
        }
        return tokenCount;
    }    
    public static void main(String[] args){
        // DynamicCommandBuffer arr = new DynamicCommandBuffer(2);
        // arr.add('1');
        // arr.add('2');
        // arr.display();
        // arr.add('3');
        // arr.display();
        Scanner sc = new Scanner(System.in);
        String input =  sc.nextLine();
        System.out.println("The input String is : " +  input);
        tokenizer(input);
    }
}
