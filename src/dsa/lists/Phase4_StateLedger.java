package dsa.lists;

public class Phase4_StateLedger {
    public static class StateLedger{
        private static class Node{
            int commandId;
            Node next;
            public Node(int commandId){
                this.commandId = commandId;
                this.next = null;
            }
        }
        private Node head;
        private Node tail;
        private int size;
        public StateLedger(){
            this.head = null;
            this.tail = null;
            this.size = 0;
        }
        //Create
        public void append(int commandId){
            Node node = new Node(commandId);
            if(head == null){
                head = node;
                tail = node;
            }else{
                tail.next = node;
                tail = node;
            }
            size++;
        }

        //Delete

        public boolean delete(int target){
            Node ptr = head;
            Node prev = null;
            boolean found = false;
            while(ptr != null){
                if (ptr.commandId == target) {
                    found = true;
                    if(prev == null){
                        head = head.next;
                        if(head == null) tail = null;
                    }else{
                        prev.next = ptr.next;
                        if(ptr.next == null){
                            tail = prev;
                        }
                    }
                    size--;
                    return found;
                }
                
                prev = ptr;
                ptr=ptr.next;
            }
            return found;
        }

        // Utility

        public int getSize(){
            return this.size;
        }

        public void display(){
            Node current = head;
            System.out.println("Engine Ledger State [" + size + " Tasks]: ");

            while(current!=null){
                System.out.print(current.commandId + " -> ");
                current = current.next;
            }

            System.out.println("null");
        }
    }
    public static void main(String[] args) {
        System.out.println("--- INITIALIZING PRODUCTION STATE LEDGER ---");
        StateLedger engineLedger = new StateLedger();
        
        System.out.println("\n--- O(1) APPEND OPERATIONS ---");
        engineLedger.append(501);
        engineLedger.append(502);
        engineLedger.append(503);
        engineLedger.display();
        
        System.out.println("\n--- SECURE DELETION ---");
        System.out.println("Deleting 502 (Middle node)...");
        engineLedger.delete(502);
        engineLedger.display();
        
        System.out.println("Deleting 503 (Tail node)...");
        engineLedger.delete(503);
        engineLedger.display();
        
        System.out.println("\n--- APPENDING AFTER DELETING TAIL ---");
        // Because our tail pointer logic is solid, this append will work flawlessly in O(1)
        engineLedger.append(999);
        engineLedger.display();
    }
}
