package com.socialsentiment.service;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


/**
 * The FinBertSentimentAnalyzer class provides functionality for analyzing financial sentiments
 * of a given textual message associated with a symbol using the FinBERT model hosted on HuggingFace.
 *
 * The class leverages the HuggingFace Inference API for processing the input text and retrieves
 * the sentiment prediction. The sentiment is categorized into three labels: bullish, bearish, or neutral,
 * based on the FinBERT model's output.
 *
 * This class acts as a service component to integrate sentiment analysis into an application.
 */
@Service
public class FinBertSentimentAnalyzer {

    private static final String API_URL = "https://api-inference.huggingface.co/models/ProsusAI/finbert";
    private static final String API_KEY = "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";  // Replace with your actual key

    private HttpClient client = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Analyzes the sentiment of a financial message associated with a specific stock symbol
     * using the FinBERT model through the HuggingFace Inference API.
     *
     * This method processes the given message and determines whether the sentiment
     * is "bullish," "bearish," or "neutral" based on the highest confidence score
     * output by the FinBERT model.
     *
     * @param symbol The stock symbol associated with the financial message (e.g., "AAPL" for Apple Inc.).
     * @param message The financial message to be analyzed for sentiment.
     * @return A string indicating the financial sentiment, which can be "bullish," "bearish," or "neutral."
     *         Returns "neutral" as a fallback in case of errors or if no sentiment could be determined.
     */
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
