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
    @Autowired
    private GptSentimentAnalyzer gptSentimentAnalyzer;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void fetchAndSave(String symbol) {
        try {
            String url = "https://api.stocktwits.com/api/2/streams/symbol/" + symbol + ".json";
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode messages = root.get("messages");

            int bullishCount = 0;
            int bearishCount = 0;
            int neutralCount = 0;

            StringBuilder combinedText = new StringBuilder();

            for (JsonNode message : messages) {
                String title = message.has("title") ? message.get("title").asText("") : "";
                String body = message.get("body").asText("");
                String fullMessage = title + " " + body;

                combinedText.append(fullMessage).append("\n\n");

                // 🔥 Call GPT for each message
                String sentiment = gptSentimentAnalyzer.analyzeSentimentWithGPT(fullMessage,symbol);

                if (sentiment.equals("bullish")) bullishCount++;
                else if (sentiment.equals("bearish")) bearishCount++;
                else neutralCount++;
            }

            // 🧠 Decide final sentiment based on majority
            String finalSentiment;
            if (bullishCount > bearishCount && bullishCount > neutralCount) finalSentiment = "bullish";
            else if (bearishCount > bullishCount && bearishCount > neutralCount) finalSentiment = "bearish";
            else finalSentiment = "neutral";

            // 📝 Save one record with combined text and final sentiment
            StockSentiment stockSentiment = new StockSentiment();
            stockSentiment.setSymbol(symbol);
            stockSentiment.setMessage(combinedText.toString());
            stockSentiment.setSentiment(finalSentiment);
            stockSentiment.setCreatedAt(LocalDateTime.now());

            repository.save(stockSentiment);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}