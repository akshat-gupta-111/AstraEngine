package engine.network;

import engine.interfaces.ILLMClient;
import engine.utils.JSONExtractor;

public class GPTLLMClient implements ILLMClient{
    private final String apiKey;
    private final String endpoint;
    public GPTLLMClient(){ 
        this.apiKey = System.getenv("AZURE_API_KEY");
        this.endpoint = "https://ai-akshatguptacsaiml248270ai011246036604.openai.azure.com/openai/deployments/gpt-4o/chat/completions?api-version=2024-12-01-preview";
    }

    @Override
    public String generateResponse(String prompt) {

        String sanitizedPrompt = prompt.replace("\\", "\\\\");
        sanitizedPrompt = sanitizedPrompt.replace("\"", "\\\"");
        sanitizedPrompt = sanitizedPrompt.replace("\n", "\\n")
                                   .replace("\r", "\\r")
                                   .replace("\t", "\\t");

        String jsonPayload = "{ \"messages\": [{ \"role\": \"user\", \"content\": \"" + sanitizedPrompt + "\" }] }";


        // 3. Open the HTTP Client
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

        // 4. Build the POST Request
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("api-key", apiKey)
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        // 5. Fire the request and capture the response
        try {
            java.net.http.HttpResponse<String> response = client.send(request,
                    java.net.http.HttpResponse.BodyHandlers.ofString());

            // 6. Return the raw JSON body if successful
            System.out.println(JSONExtractor.extractAzureText(response.body()));
            if (response.statusCode() != 200) {
                throw new RuntimeException("Azure API Server Overloaded or Failed. HTTP Status: " + response.statusCode());
            }
            return JSONExtractor.extractAzureText(response.body());

        } catch (java.io.IOException e) {
            System.err.println("Network I/O error: " + e.getMessage());
            throw new RuntimeException("Failed to reach Azure API due to network error", e);
        } catch (InterruptedException e) {
            System.err.println("Request was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupted status
            throw new RuntimeException("Azure API request was interrupted", e);
        }
    }


}
