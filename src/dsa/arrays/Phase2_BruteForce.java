package dsa.arrays;

public class Phase2_BruteForce{
    public static void main(String[] agrs){
        int[] systemCommandBuffer = new int[5];
        int currentSize = 0;

        systemCommandBuffer[0] = 100;
        currentSize++;
        systemCommandBuffer[1] = 101;
        currentSize++;
        systemCommandBuffer[2] = 102;
        currentSize++;

        System.out.println("\n--- STEP 2: MANUAL READING ---");
        System.out.println("Slot 0 contains: " + systemCommandBuffer[0]);
        System.out.println("Slot 1 contains: " + systemCommandBuffer[1]);
        System.out.println("Slot 2 contains: " + systemCommandBuffer[2]);
        System.out.println("Total valid elements in buffer: " + currentSize);

        System.out.println("\n--- STEP 3: DIRECT OVERWRITE ---");
        systemCommandBuffer[0] = 99;
        System.out.println("Slot 0 updated value is now: " + systemCommandBuffer[0]);

        System.out.println("\n--- STEP 4: CHAR ARRAY REPRESENTATION ---");
        char[] systemStatus = {'R','U','N','N','I','N','G'};

        System.out.print("System Status Array: ");
        System.out.print(systemStatus[0]);
        System.out.print(systemStatus[1]);
        System.out.print(systemStatus[2]);
        System.out.println("");

    }
}