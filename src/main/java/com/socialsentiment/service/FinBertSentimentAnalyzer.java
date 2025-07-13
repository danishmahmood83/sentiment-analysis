package com.socialsentiment.service;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class FinBertSentimentAnalyzer {

    private static final String API_URL = "https://api-inference.huggingface.co/models/ProsusAI/finbert";
    private static final String API_KEY = "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";  // Replace with your actual key

    private HttpClient client = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();


    public String analyzeSentimentWithFinBERT(String symbol, String message) {
        try {

            String input = String.format("Financial sentiment for %s: %s", symbol, message);

            String requestBody = objectMapper.createObjectNode()
                    .put("inputs", input)
                    .toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());

            if (root.isArray() && root.size() > 0 && root.get(0).isArray()) {
                JsonNode predictions = root.get(0);

                String topLabel = null;
                double topScore = -1.0;

                for (JsonNode prediction : predictions) {
                    String label = prediction.get("label").asText().toLowerCase();
                    double score = prediction.get("score").asDouble();

                    if (score > topScore) {
                        topScore = score;
                        topLabel = label;
                    }
                }

                return switch (topLabel) {
                    case "positive" -> "bullish";
                    case "negative" -> "bearish";
                    default -> "neutral";
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "neutral"; // fallback
    }
}
