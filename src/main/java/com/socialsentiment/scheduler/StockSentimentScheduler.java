package com.socialsentiment.scheduler;

import com.socialsentiment.service.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockSentimentScheduler {
    @Autowired
    SentimentService sentimentService;

    @Scheduled(fixedRate = 60000)
    public void scheduledFetch() {
        sentimentService.fetchAndSave("AAPL");
        sentimentService.fetchAndSave("TSLA");
        sentimentService.fetchAndSave("NVDA");
    }
}