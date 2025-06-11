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

@RestController
@RequestMapping("/api/sentiment")
public class SentimentController {

    @Autowired
    private StockSentimentRepository repository;

    @Autowired
    private SentimentService service;

    @GetMapping("/{symbol}")
    public List<StockSentiment> getSentiments(@PathVariable String symbol) {
        return repository.findBySymbol(symbol);
    }

    @GetMapping("/{symbol}/summary")
    public Map<String, Long> getSentimentSummary(@PathVariable String symbol) {
        List<StockSentiment> sentiments = repository.findBySymbol(symbol);
        return sentiments.stream()
                .collect(Collectors.groupingBy(StockSentiment::getSentiment, Collectors.counting()));
    }

    @PostMapping("/{symbol}/fetch")
    public ResponseEntity<String> fetchAndSaveNow(@PathVariable String symbol) {
        service.fetchAndSave(symbol);
        return ResponseEntity.ok("Fetched and saved sentiment for: " + symbol);
    }
}
