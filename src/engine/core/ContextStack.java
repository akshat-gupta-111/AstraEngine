package engine.core;

public class ContextStack {
    private static class Stack{
        private class Node{
            private String context;
            private Node next;
            private Node(String context){
                this.context = context;
                this.next = null;
            }
        }
        private Node head;
        private int size;

        private Stack(){
            this.head = null;
            this.size = 0;
        }

        private void push(String data){
            Node node = new Node(data);
            if(head == null){
                head = node;
            }else{
                node.next = head;
                head = node;
            }
            size++;
            System.out.println("Pushed the data at TOP: " + data);
        }

        private String pop(){
            if(size == 0){
                System.out.println("Empty Stack!");
                return "null";
            }
            String poppedContext = head.context;
            head = head.next;
            size--;
            return poppedContext;
        }

        private String peek(){
            if(size == 0){
                System.out.println("Empty Stack!");
                return "null";
            }
            return head.context;
        }

        private void display(){
            Node ptr = head;
            if(ptr == null){
                System.out.println("Empty Stack");
                return;
            }
            System.out.print("TOP -> ");
            while(ptr != null){
                System.out.print(ptr.context + " -> ");
                ptr = ptr.next;
            }
            System.out.print("Bottom");
        }
    }
    public static void main(String args[]){
        Stack context = new Stack();
        context.display();
        context.push("RESEARCH");
        context.push("SCRAPEWEB");
        System.out.println("Popped : "+ context.pop());
        System.out.println("At top : "+ context.peek());
        context.display();
    }
}
