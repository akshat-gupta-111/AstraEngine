package engine.cache;

import java.util.HashMap;
import java.util.LinkedList;

import engine.interfaces.ICache;

public class EngineMemoryCache implements ICache{
    private class Node{
        private String key;
        private String value;
        private Node next;
        private Node prev;

        public Node(String key, String value){
            this.key = key;
            this.value = value;
            this.next = null;
            this.prev = null;
        }
    }
    private HashMap<String, Node> map;
    private Node head;
    private Node tail;
    private int capacity;

    public EngineMemoryCache(int capacity){
        this.capacity = capacity;
        this.map = new HashMap<>();
        head = new Node("HEAD", "DUMMY_NODE");
        tail = new Node("TAIL", "DUMMY_NODE");
        head.next = tail;
        tail.prev = head;
    }

    private void removeNode(Node curr){
        Node ptr = curr.prev;
        ptr.next = curr.next;
        curr.next.prev = ptr;
    }

    private void insertToFront(Node node){
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
        node.prev = head;
    }

    public synchronized void put(String key, String value){
        Node node = new Node(key, value);
        if(map.containsKey(key)) removeNode(map.get(key));
        insertToFront(node);
        map.put(key, node);

        if(map.size() > capacity){
            Node lru = tail.prev;
            removeNode(lru);
            map.remove(lru.key);
        }
    }
    public synchronized String get(String key){
        if(!map.containsKey(key)) return "DOES_NOT_EXISTS";
        Node node = map.get(key);
        removeNode(node);
        insertToFront(node);
        return node.value;
    }
}
