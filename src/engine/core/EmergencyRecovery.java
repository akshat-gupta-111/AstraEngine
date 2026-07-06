package engine.core;
import engine.core.ContextStack.Stack;
import engine.core.PriorityEngine.CustomHeap;
import engine.core.RollbackLedger.Ledger;

public class EmergencyRecovery {
    public static void main(String agrs[]){
        Stack context = new Stack();
        context.push("WORKFLOW_EXECUTION");
        context.display();

        CustomHeap engine = new CustomHeap(3);
        engine.schedule("Data_Ingest", 10);
        engine.schedule("Rogue_Task_A", 50);
        engine.schedule("Safe_Task_B", 20);
        engine.display();

        engine.dispatch();
        System.out.println("[CRASH] Rogue Task A triggered an infinite Graph cycle!");

        Ledger ledger = new Ledger();
        ledger.recordTask("State_1_Clean");
        ledger.recordTask("State_2_Clean");
        ledger.recordTask("State_3_PreCrash");
        ledger.display();
        ledger.undo(1);
        context.pop();
    }
}
