package com.socialsentiment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Service that provides sentiment analysis for financial text data
 * using the OpenAI GPT API. This class integrates with GPT models
 * to evaluate the sentiment of a given text concerning a specific
 * stock symbol and returns the sentiment as one of three possible
 * values: bullish, bearish, or neutral.
 */
@Service
public class GptSentimentAnalyzer {


    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Analyzes the sentiment of the provided text in relation to a given stock symbol
     * using the GPT-4 model via the OpenAI API. The method interprets the sentiment
     * in the context of the stock symbol and returns the result as one of three possible
     * values: "bullish", "bearish", or "neutral".
     *
     * @param inputText the message or text to be analyzed, which provides context for sentiment analysis
     * @param symbol the stock symbol against which the sentiment of the text is evaluated
     * @return a string representing the sentiment of the provided text in relation to the stock symbol.
     *         Returns one of "bullish" (positive sentiment), "bearish" (negative sentiment),
     *         or "neutral" (no definitive sentiment).
     */
    public String analyzeSentimentWithGPT(String inputText,String symbol) {
        try {
            // Prompt to send
            String prompt = String.format(
                    """
                    Analyze the sentiment of the following message with respect to the stock symbol "%s".Message: "%s" Respond with only one word: bullish, bearish, or neutral.
                    """, symbol, inputText
            );

            // Construct the JSON payload properly
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

            String requestBody = mapper.writeValueAsString(root);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Authorization", "Bearer " + "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")
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
