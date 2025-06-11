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

@Service
public class SentimentService {

    @Autowired
    private StockSentimentRepository repository;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void fetchAndSave(String symbol) {
        try {
            String url = "https://api.stocktwits.com/api/2/streams/symbol/" + symbol + ".json";
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String combinedText="";
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode messages = root.get("messages");

            for (JsonNode message : messages) {
                // Title extraction (some messages may not have this)
                String title = "";
                if (message.has("title")) {
                    title = message.get("title").asText("");
                }

                String body = message.get("body").asText("");

                // sentiment.basic field
                String sentimentValue = "";
                JsonNode entitiesNode = message.get("entities");
                if (entitiesNode != null) {
                    JsonNode sentimentNode = entitiesNode.get("sentiment");
                    if (sentimentNode != null && sentimentNode.has("basic")) {
                        sentimentValue = sentimentNode.get("basic").asText("");
                    }
                }

                combinedText += title + " " + body + " " + sentimentValue;
            }
                String finalSentiment = analyzeSentiment(combinedText);

                StockSentiment stockSentiment = new StockSentiment();
                stockSentiment.setSymbol(symbol);
                stockSentiment.setMessage(combinedText);
                stockSentiment.setSentiment(finalSentiment);
                stockSentiment.setCreatedAt(LocalDateTime.now());

                repository.save(stockSentiment);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String analyzeSentiment(String text) {
        text = text.toLowerCase();

        int bullishScore = 0;
        int bearishScore = 0;

        String[] bullishWords = {"buy", "bullish", "long", "green", "up", "rocket", "moon"};
        String[] bearishWords = {"sell", "bearish", "short", "red", "down", "crash"};

        for (String word : bullishWords) {
            if (text.contains(word)) bullishScore++;
        }
        for (String word : bearishWords) {
            if (text.contains(word)) bearishScore++;
        }

        if (bullishScore > bearishScore) return "bullish";
        if (bearishScore > bullishScore) return "bearish";
        return "neutral";
    }
}