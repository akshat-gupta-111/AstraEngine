package dsa.heaps;

import java.util.HashMap;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Phase5_Advanced {
    public static class LRUCache{
        public class Node{
            public String key;
            public String value;
            public Node next;
            public Node prev;
            public Node(String key, String value){
                this.key = key;
                this.value = value;
                this.next = null;
                this.prev = null;
            }
        }
        private final int capacity;
        private HashMap<String, Node> map;
        private Node head, tail;

        public LRUCache(int capacity){
            this.capacity = capacity;
            this.map = new HashMap<>();

            head = new Node("Start", "Head");
            tail = new Node("End", "Tail");

            head.next = tail;
            tail.prev = head;
        }

        private void removeNode(Node node){
            Node ptr = node.prev;
            ptr.next = node.next;
            node.next.prev = ptr;
        }

        private void insertToFront(Node node){
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
            node.prev = head;
        }

        public String get(String key){
            if(!map.containsKey(key)) return "NOT_Found";

            Node node = map.get(key);
            removeNode(node);
            insertToFront(node);
            return node.value;
        }

        public void put(String key, String value){
            if(map.containsKey(key)){
                removeNode(map.get(key));
            }
            Node node = new Node(key, value);
            insertToFront(node);
            map.put(key, node);

            if(map.size() > capacity){
                Node lru = tail.prev;
                removeNode(lru);
                map.remove(lru.key);
                System.out.println("[CACHE EVICTION] Purged old memory: " + lru.key);
            }
        }
    }
    public static void printTop3SlowestTasks(int[] tasks){
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int k = 3;

        for(int task : tasks){
            minHeap.add(task);
            if(minHeap.size()>k){
                minHeap.poll(); //pops the roots(minimum values)
            }
        }
        System.out.println("Slowest Tasks : " + minHeap);
    }

    public static void main(String[] args) {
        System.out.println("====== ASTRA ENGINE: LRU CACHE ======");
        LRUCache engineCache = new LRUCache(2); // Can only hold 2 items!
        
        engineCache.put("USER_1", "Alice");
        engineCache.put("USER_2", "Bob");
        System.out.println("Fetched: " + engineCache.get("USER_1")); // USER_1 is now Most Recently Used!
        System.out.println("Fetched: " + engineCache.get("USER_2"));
        engineCache.put("USER_3", "Charlie"); // This triggers an eviction!

        System.out.println("Fetching USER_2 (Bob): " + engineCache.get("USER_2")); 


        System.out.println("\n====== ASTRA ENGINE: TOP-K HEAP ======");
        int[] streamOfTasks = {15, 200, 10, 50, 450, 30, 800, 5};
        System.out.println("Finding the top 3 slowest tasks out of a massive stream...");
        printTop3SlowestTasks(streamOfTasks); // Should output [200, 450, 800]
    }
}
