package engine.network;

import engine.interfaces.ILLMClient;

public class SimulatedLLMClient implements ILLMClient{
    public String generateResponse(String prompt){
        String result = "";
        System.out.println("[NETWORK] Transmitting prompt: " + prompt);
        try{
            Thread.sleep(100);
            result = "{ \"status\": \"success\", \"data\": \"Processed: " + prompt + "\" }";
        }catch (InterruptedException e){
            System.out.println("Got the exception : " + e);
            result = "{ \"status\": \"failed\", \"data\": \"Processed: " + prompt + "\" }";
        }
        return result;
    }
}
