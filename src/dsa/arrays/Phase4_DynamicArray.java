package dsa.arrays;

public class Phase4_DynamicArray {
    public static class DynamicCommandBuffer{
        private int[] buffer;
        private int size;
        private int capacity;

        public DynamicCommandBuffer(int initialCapacity){
            this.capacity = initialCapacity;
            this.size = 0;
            this.buffer = new int[capacity];
        }

        // Create

        public void add(int commandId){
            if(size == capacity){
                grow();
            }
            buffer[size] = commandId;
            size++;
        }

        public void insert(int index, int commandId){
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
        public void update(int updateIndex, int updatedValue){
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
            buffer[size - 1] = 0;
            size--;
        }
        private void grow(){
            int newCapacity = capacity * 2;
            int[] newBuffer = new int[newCapacity];
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
    public static void main(String[] args){
        System.out.println("--- INITIALIZING PRODUCTION BUFFER (Capacity: 2) ---");
        DynamicCommandBuffer engineBuffer = new DynamicCommandBuffer(2);
        engineBuffer.display();

        System.out.println("\n--- PUMPING COMMAND DATA ---");
        engineBuffer.add(501);
        engineBuffer.add(502);
        engineBuffer.display();

        System.out.println("\n--- TRIGGERING AUTOMATIC RESIZE ON NEXT ADD ---");
        engineBuffer.add(503); // This will force a grow() operation
        engineBuffer.display();

        System.out.println("\n--- EXECUTING SECURE INTERMEDIATE INSERTION ---");
        engineBuffer.insert(0, 777);
        engineBuffer.display();

        System.out.println("\n--- EXECUTING CLEAN DELETION ---");
        engineBuffer.delete(2);
        engineBuffer.display();
    }
}
