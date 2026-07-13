package engine.utils;

public class JSONExtractor {
    public static String extractGeminiText(String raw){
        if(raw == null || raw.isEmpty()){
            return "";
        }
        String target = "\"text\": \"";
        int startIndex = raw.indexOf(target);
        if(startIndex == -1) return raw;
        startIndex += target.length();
        int endIndex = raw.indexOf("\"", startIndex);

        String extracted = raw.substring(startIndex, endIndex);
        return extracted.replace("\\n", "\n");
    }

    
    public static String extractAzureText(String raw){
        if(raw == null || raw.isEmpty()){
            return "";
        }
        String target = "\"content\":\"";
        int startIndex = raw.indexOf(target);
        if(startIndex == -1) return raw;
        startIndex += target.length();
        int endIndex = raw.indexOf("\"", startIndex);

        String extracted = raw.substring(startIndex, endIndex);
        return extracted.replace("\\n", "\n");
    }
}
