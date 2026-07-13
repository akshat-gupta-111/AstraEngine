package engine.network;

import engine.utils.JSONExtractor;
import engine.interfaces.ILLMClient;
import java.net.http.HttpClient;

public class GeminiLLMClient implements ILLMClient {
    private final String apiKey;
    private final String endpoint;

    public GeminiLLMClient() {
        this.apiKey = System.getenv("GEMINI_API_KEY");
        this.endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key="
                + this.apiKey;
    }

   
   
    @Override
    public String generateResponse(String prompt) {

        String sanitizedPrompt = prompt.replace("\"", "\\\"");

        String jsonPayload = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + sanitizedPrompt + "\" }] }] }";

        // 3. Open the HTTP Client
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

        // 4. Build the POST Request
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        // 5. Fire the request and capture the response
        try {
            java.net.http.HttpResponse<String> response = client.send(request,
                    java.net.http.HttpResponse.BodyHandlers.ofString());

            // 6. Return the raw JSON body if successful
            System.out.println(JSONExtractor.extractGeminiText(response.body()));
            if (response.statusCode() != 200) {
                throw new RuntimeException("API Server Overloaded or Failed. HTTP Status: " + response.statusCode());
            }
            return JSONExtractor.extractGeminiText(response.body());

        } catch (java.io.IOException e) {
            System.err.println("Network I/O error: " + e.getMessage());
            throw new RuntimeException("Failed to reach Gemini API due to network error", e);
        } catch (InterruptedException e) {
            System.err.println("Request was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupted status
            throw new RuntimeException("Gemini API request was interrupted", e);
        }
    }



}
