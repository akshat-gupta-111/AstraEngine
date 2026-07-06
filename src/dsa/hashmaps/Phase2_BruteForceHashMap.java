package dsa.hashmaps;

public class Phase2_BruteForceHashMap {
    public static class ContextNode{
        public String key;
        public String value;
        public ContextNode next;
        public ContextNode(String key, String value){
            this.key = key;
            this.value = value;
            this.next = null;
        } 
    }
    static int capacity = 5;
    static ContextNode[] memorySlots = new ContextNode[capacity];

    static int calculateIndex(String key){
        int asciisum = 0;
        for(int i = 0; i< key.length(); i++){
            asciisum += key.charAt(i);
        }
        return asciisum % capacity;
    }

    static void put(String key, String value){
        ContextNode node = new ContextNode(key, value);

        int index = calculateIndex(key);

        if(memorySlots[index] == null){
            memorySlots[index] = node;
            return;
        }
        ContextNode ptr = memorySlots[index];
        
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

    static String get(String key){
        String result = "null";
        int index = calculateIndex(key);
        if(memorySlots[index] == null){
            System.out.println("Key does not exist!");
        }else{
            ContextNode ptr = memorySlots[index];
            while(ptr != null){
                if(ptr.key.equals(key)){
                    result = ptr.value;
                    break;
                }
                ptr = ptr.next;
            }
        }
        return result;
    }
    static void display(){
        for(int i = 0; i< capacity; i++){
            System.out.print(i+ "th slot contains : ");
            ContextNode ptr = memorySlots[i];
            while (ptr!= null) {
                System.out.print(ptr.key +":"+ptr.value + " - ");
                ptr = ptr.next;
            }
            System.out.println("null");
        }
    }
     public static void main(String[] args) {
        System.out.println("====== INITIALIZING BRUTE FORCE HASHMAP ======");
        
        // 1. Standard Insertions
        put("SYSTEM_ENV", "PROD");
        put("MAX_THREADS", "64");
        
        // 2. These two specific keys are mathematically calculated to cause a collision
        // depending on the ASCII math and modulo 5. Watch the console!
        put("API_KEY", "XYZ_992"); 
        put("DB_PASS", "ADMIN_123"); 
        put("TIMEOUT", "5000");

        // Let's look at the actual RAM Layout
        display();

        // 3. Instant Retrieval Retrieval
        System.out.println("Fetching MAX_THREADS: " + get("MAX_THREADS"));
        System.out.println("Fetching DB_PASS (Inside a chain!): " + get("TIMEOUT"));
    }
}