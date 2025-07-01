package com.socialsentiment.service;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialsentiment.entity.StockSentiment;
import com.socialsentiment.repository.StockSentimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

/**
 * Service that manages the process of fetching, analyzing, and storing
 * stock sentiment data from external APIs. Integrates with external
 * sentiment analysis providers and a persistence layer to evaluate and
 * save sentiment data.
 */
@Service
public class SentimentService {

    @Autowired
    private StockSentimentRepository repository;
    @Autowired
    private GptSentimentAnalyzer gptSentimentAnalyzer;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Fetches stock-related messages from an external API, performs sentiment analysis
     * on the messages, and saves them to the repository if they do not already exist.
     *
     * @param symbol The stock symbol for which the messages and sentiment analysis should be fetched and saved.
     */
    public void fetchAndSave(String symbol) {
        try {
            String url = "https://api.stocktwits.com/api/2/streams/symbol/" + symbol + ".json";
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode messages = root.get("messages");


            for (JsonNode message : messages) {
                long messageId = message.get("id").asLong();

                // ✅ Skip if message already exists
                if (repository.existsByMessageId(messageId)) {
                    continue;
                }

                String title = message.has("title") ? message.get("title").asText("") : "";
                String body = message.get("body").asText("");
                String fullMessage = (title + " " + body).trim();

                // 🔥 Call GPT for sentiment analysis
                String sentiment = gptSentimentAnalyzer.analyzeSentimentWithGPT(fullMessage, symbol);

                // 💾 Save each message individually
                StockSentiment stockSentiment = new StockSentiment();
                stockSentiment.setSymbol(symbol);
                stockSentiment.setMessage(fullMessage);
                stockSentiment.setSentiment(sentiment);
                stockSentiment.setMessageId(messageId);
                stockSentiment.setCreatedAt(LocalDateTime.now());

                repository.save(stockSentiment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}