package dsa.lists;

public class Phase2_BruteForce{
    public static class Node{
        public int commandId;
        public Node next;
        public Node(int data){
            this.commandId = data;
            this.next = null;
        }
    }
    public static void main(String[] args) {
        System.out.println("--- STEP 1: SCATTERED ALLOCATION ---");
        Node headNode = new Node(101);
        Node secNode = new Node(102);
        Node thNode = new Node(103);

        System.out.println("Created Head Node: " + headNode.commandId);
        System.out.println("Created Second Node: " + secNode.commandId);
        System.out.println("Created Third Node: " + thNode.commandId);

        headNode.next = secNode;
        secNode.next = thNode;

        System.out.println("Pointers connected. The list is now: 101 -> 202 -> 303 -> null");

        System.out.println("\n--- STEP 3: BRUTE FORCE TRAVERSAL ---");

        Node currentPointer = headNode;

        while (currentPointer != null) {
            System.out.print(currentPointer.commandId + " -> ");
            currentPointer = currentPointer.next;
        }
        System.out.println("null");


        Node emergencyNode = new Node(999);

        emergencyNode.next = headNode.next;

        headNode.next = emergencyNode;

        System.out.print("Rewired List: ");
        currentPointer = headNode;
        while (currentPointer != null) {
            System.out.print("[" + currentPointer.commandId + "] -> ");
            currentPointer = currentPointer.next;
        }
        System.out.println("null");

    }
}