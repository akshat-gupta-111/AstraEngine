package engine.core;

import engine.models.EngineTask;

public class WorkStealingDeque {
    private class Node{
        EngineTask task;
        Node next;
        Node prev;
        public Node(EngineTask task){
            this.task = task;
            this.next = null;
            this.prev = null;
        }
    }

    private Node front;
    private Node rear;
    private int size;
    public WorkStealingDeque(){
        this.front = null;
        this.rear = null;
        this.size = 0;
    }
    public synchronized void addFront(EngineTask task){
        Node node = new Node(task);
        if(front == null){
            front = node;
            rear = node;
        }else{
            node.next = front;
            front.prev = node;
            front = node;
        }
        size++;
    }

    public synchronized void addRear(EngineTask task){
        Node node = new Node(task);
        if(rear == null){
            front = rear = node;
        }else{
            rear.next = node;
            node.prev = rear;
            rear = node;
        }
        size++;
    }

    public synchronized EngineTask removeFront(){
        if(front == null){
            
            return null;
        }
        EngineTask task = front.task;
        front = front.next;
        if(front == null) rear = null;
        else front.prev = null;
        size--;
        return task;
    }

    public synchronized EngineTask removeLast(){
        if(rear == null){
           
            return null;
        }
        EngineTask task = rear.task;
        rear = rear.prev;
        if(rear == null) front = null;
        else rear.next = null;
        size--;
        return task;
    }

    public synchronized int getSize(){
        return size;
    }

    public synchronized boolean isEmpty(){
        return size == 0;
    }

    public boolean stealFrom(WorkStealingDeque victim){
        EngineTask stolenTask = victim.removeLast();
        if(stolenTask != null){
            this.addFront(stolenTask);
            return true;
        }
        return false;
    }
}
