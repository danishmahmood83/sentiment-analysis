package com.socialsentiment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialsentiment.entity.StockSentiment;
import com.socialsentiment.repository.StockSentimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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
    @Autowired
    private CoreNlpSentimentAnalyzer coreNlpSentimentAnalyzer;
    @Autowired
    private FinBertSentimentAnalyzer finBertSentimentAnalyzer;

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

                boolean gptExists = repository.existsByMessageIdAndAnalysisMethod(messageId, "gpt");
                boolean stanfordExists = repository.existsByMessageIdAndAnalysisMethod(messageId, "stanford");
                boolean finbertExists = repository.existsByMessageIdAndAnalysisMethod(messageId, "finbert");

                if (gptExists && stanfordExists && finbertExists) {
                    continue;
                }

                String title = message.has("title") ? message.get("title").asText("") : "";
                String body = message.get("body").asText("");
                String fullMessage = (title + " " + body).trim();

                LocalDateTime createdAt = LocalDateTime.now();

                if (!gptExists) {
                    String gptSentiment = gptSentimentAnalyzer.analyzeSentimentWithGPT(fullMessage, symbol);
                    saveSentimentRecord(symbol, fullMessage, messageId, gptSentiment, "gpt", createdAt);
                }

                if (!stanfordExists) {
                    String stanfordSentiment = coreNlpSentimentAnalyzer.analyzeSentiment(fullMessage,symbol);
                    saveSentimentRecord(symbol, fullMessage, messageId, stanfordSentiment, "stanford", createdAt);
                }

                if (!finbertExists) {
                    String finbertSentiment = finBertSentimentAnalyzer.analyzeSentimentWithFinBERT(symbol, fullMessage);
                    saveSentimentRecord(symbol, fullMessage, messageId, finbertSentiment, "finbert", createdAt);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSentimentRecord(String symbol, String message, long messageId,
                                     String sentiment, String analysisMethod, LocalDateTime createdAt) {
        if (!repository.existsByMessageIdAndAnalysisMethod(messageId, analysisMethod)) {
            StockSentiment record = new StockSentiment();
            record.setSymbol(symbol);
            record.setMessage(message);
            record.setSentiment(sentiment);
            record.setAnalysisMethod(analysisMethod);
            record.setMessageId(messageId);
            record.setCreatedAt(createdAt);
            repository.save(record);
        }
    }
}