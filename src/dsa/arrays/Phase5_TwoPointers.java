package dsa.arrays;

public class Phase5_TwoPointers {
    public static void reverseLedger(int[] ledger){
        int left = 0;
        int right = ledger.length - 1;
        while(left < right){
            int temp = ledger[left];
            ledger[left] = ledger[right];
            ledger[right] = temp;
            left++;
            right--;
        }
    }
    public static void main(String[] args) {
        int[] executionLedger = {101, 102, 103, 104, 105};
        System.out.println("Original Ledger: [101, 102, 103, 104, 105, ]");
        
        reverseLedger(executionLedger);
        
        System.out.print("Reversed Ledger: [");
        for(int command : executionLedger) System.out.print(command + ", ");
        System.out.println("]");
    }
}
