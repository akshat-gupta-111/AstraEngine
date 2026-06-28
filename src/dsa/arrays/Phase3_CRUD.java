package dsa.arrays;

public class Phase3_CRUD {
    public static void main(String[] args) {
        int[] commandStorage = new int[8];
        commandStorage[0] = 100;
        commandStorage[1] = 101;
        commandStorage[2] = 102;
        commandStorage[3] = 103;
        int currentSize = 4;

        System.out.println("--- INITIAL STATE ---");
        printBuffer(commandStorage, currentSize);

        // Create
        int insertIndex = 2;
        int newCommand = 202;

        System.out.println("\n--- 1. CREATE: Inserting " + newCommand + " at Index " + insertIndex + " ---");

        for(int i = currentSize - 1; i >= insertIndex; i--){
            commandStorage[i + 1] = commandStorage[i];
        }

        commandStorage[insertIndex] = newCommand;
        currentSize++;
        printBuffer(commandStorage, currentSize);


        // READ

        int target = 104;
        System.out.println("\n--- 2. READ: Linear Searching for Command " + target + " ---");
        int found = -1;

        for(int i = 0; i < currentSize; i++){
            if(commandStorage[i] == target){
                found = i;
                break;
            }
        }
        System.out.println("Command " + target + " located at Memory Index: " + found);

        //Update 

        int updateIndex = 3;
        int updatedValue = 999;

        System.out.println("\n--- 3. UPDATE: Changing Index " + updateIndex + " to " + updatedValue + " ---");

        commandStorage[updateIndex] = updatedValue;

        printBuffer(commandStorage, currentSize);

        //Delete 

        int deleteIndex = 2;
        System.out.println("\n--- 4. DELETE: Removing Element at Index " + deleteIndex + " ---");

        for(int i = deleteIndex; i < currentSize; i++ ){
            commandStorage[i] = commandStorage[i + 1];
        }

        commandStorage[currentSize - 1] = 0;
        currentSize--;

        printBuffer(commandStorage, currentSize);
    }

    private static void printBuffer(int[] arr, int size){
        System.out.print("RAM Layout: [ ");
        for(int i = 0; i< arr.length; i++){
            if(i < size){
                System.out.print(arr[i] + " ");
            }else{
                System.out.print("__ ");
            }
        }
        System.out.println("] | Total Valid Elements: " + size);
    }
}