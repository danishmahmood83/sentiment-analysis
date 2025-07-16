package com.socialsentiment.controller;

import com.socialsentiment.entity.StockSentiment;
import com.socialsentiment.repository.StockSentimentRepository;
import com.socialsentiment.service.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// add import for slf4j logger via lombok
import lombok.extern.slf4j.Slf4j;




/**
 * This controller provides REST endpoints for managing and interacting with stock sentiment data.
 * It supports obtaining sentiment details, providing a sentiment summary, and fetching sentiment data for a specific stock symbol.
 */
@RestController
@RequestMapping("/api/sentiment")
@Slf4j // enables us of log for structured logging 
public class SentimentController {

    /**
     * The {@code StockSentimentRepository} instance used for performing data access operations
     * related to {@code StockSentiment} entities in the application.
     */
    @Autowired
    private StockSentimentRepository repository;

    /**
     * The {@code service} variable is an instance of {@link SentimentService}, which encapsulates the core business
     * logic for handling stock sentiment data. It provides functionality to interact with, analyze, and fetch real-time
     * sentiment data for stocks.
     */
    @Autowired
    private SentimentService service;

    /**
     * Retrieves a list of stock sentiment data for the specified symbol.
     *
     * @param symbol the stock symbol for which sentiment data is to be retrieved
     * @return a list of {@code StockSentiment} objects associated with the given symbol
     */
    @GetMapping("/{symbol}")
    public List<StockSentiment> getSentiments(@PathVariable String symbol) {
        log.info("Fetching sentiments for symbol: {}", symbol); //log fetch
        return repository.findBySymbol(symbol);
    }

    /**
     * Retrieves a summary of stock sentiment data for the given symbol.
     * The summary is represented as a map where the keys are sentiment categories (e.g., positive, neutral, negative)
     * and the values are counts of how many entries correspond to each sentiment.
     *
     * @param symbol the stock symbol for which sentiment summary is to be calculated
     * @return a map with sentiment categories as keys and their respective counts as values
     */
    @GetMapping("/{symbol}/summary")
    public Map<String, Long> getSentimentSummaryByMethod(
            @PathVariable String symbol,
            @RequestParam(required = false) String analysisMethod) {
        log.info("Summarizing  sentiments for symbol: {}, method: {}", symbol, analysisMethod); // log context
        List<StockSentiment> sentiments;
        if (analysisMethod != null && !analysisMethod.isEmpty()) {
            sentiments = repository.findBySymbolAndAnalysisMethod(symbol, analysisMethod);
        } else {
            sentiments = repository.findBySymbol(symbol);
        }
        return sentiments.stream()
                .collect(Collectors.groupingBy(StockSentiment::getSentiment, Collectors.counting()));
    }

    /**
     * Fetches and processes sentiment data for the given stock symbol in real time.
     * The sentiment data is analyzed and saved into the repository.
     *
     * @param symbol the stock symbol for which sentiment data is to be fetched and saved
     * @return a {@code ResponseEntity<String>} indicating the success message for the operation
     */
    @PostMapping("/{symbol}/fetch")
    public ResponseEntity<String> fetchAndSaveNow(@PathVariable String symbol) {
        log.info("Triggering sentiment fetch and save for symbol: {}, method: {}", symbol); // log action
        service.fetchAndSave(symbol);
        return ResponseEntity.ok("Fetched and saved sentiment for: " + symbol);
    }
}
