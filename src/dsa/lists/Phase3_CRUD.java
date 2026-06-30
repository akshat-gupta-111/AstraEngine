package dsa.lists;

public class Phase3_CRUD {
    public static class Node{
        public int commandId;
        public Node next;
        public Node(int data){
            this.commandId = data;
            this.next = null;
        }
    }
    private static void printList(Node head){
        Node curr = head;
        while(curr != null){
            System.out.print(curr.commandId + " -> ");
            curr = curr.next;
        }
        System.out.println("null");
    }

    public static void main(String[] args){
        //Initialize

        Node head = new Node(101);
        head.next = new Node(102);
        head.next.next = new Node(103);

        printList(head);


        // Create

        Node tail = new Node(104);
        Node curr = head;
        while(curr.next != null){
            curr = curr.next;
        }
        curr.next = tail;

        printList(head);


        // Read

        Node ptr = head;
        boolean found = false;
        int location = 0;

        while(ptr != null){
            if(ptr.commandId == 102){
                found = true;
                System.out.println("found 102 at : " + location);
            }
            location++;
            ptr = ptr.next;
        }
        if(!found) System.out.println("102 not found!");


        //Update

        int updatedValue = 999;

        ptr = head;
        while(ptr != null){
            if(ptr.commandId == 103){
                ptr.commandId = updatedValue;
                break;
            }
            ptr = ptr.next;
        }
        printList(head);


        // Delete 102

        ptr = head;

        Node prev = null;

        while(ptr != null){
            if(ptr.commandId == 102){
                if(prev == null){
                    head = head.next;
                }else{
                    prev.next = ptr.next;
                }
                
                break;
            }
            prev = ptr;
            ptr = ptr.next;
        }
        printList(head);
    }
}
