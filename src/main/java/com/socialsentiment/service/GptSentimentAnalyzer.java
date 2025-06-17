package com.socialsentiment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GptSentimentAnalyzer {


    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String analyzeSentimentWithGPT(String inputText,String symbol) {
        try {
            // Prompt to send
            String prompt = String.format(
                    """
                    Analyze the sentiment of the following message with respect to the stock symbol "%s".Message: "%s" Respond with only one word: bullish, bearish, or neutral.
                    """, symbol, inputText
            );

            // Construct the JSON payload properly
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("model", "gpt-4");
            root.put("temperature", 0.2);

            ArrayNode messages = mapper.createArrayNode();

            ObjectNode systemMessage = mapper.createObjectNode();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a financial sentiment analysis assistant.");

            ObjectNode userMessage = mapper.createObjectNode();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);

            messages.add(systemMessage);
            messages.add(userMessage);

            root.set("messages", messages);

            String requestBody = mapper.writeValueAsString(root);  // ✅ Proper JSON

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Authorization", "Bearer " + "asasas")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode jsonResponse = mapper.readTree(response.body());
            String content = jsonResponse
                    .get("choices").get(0)
                    .get("message").get("content")
                    .asText()
                    .toLowerCase();

            if (content.contains("bullish")) return "bullish";
            else if (content.contains("bearish")) return "bearish";
            else return "neutral";

        } catch (Exception e) {
            e.printStackTrace();
            return "neutral"; // fallback
        }
    }

}
