package com.socialsentiment.scheduler;

import com.socialsentiment.entity.TrackedSymbol;
import com.socialsentiment.repository.TrackedSymbolRepository;
import com.socialsentiment.service.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockSentimentScheduler {
    @Autowired
    SentimentService sentimentService;
    @Autowired
    private TrackedSymbolRepository trackedSymbolRepository;

    //@Scheduled(fixedRate = 60000)
    public void scheduledFetch() {
        List<TrackedSymbol> symbols = trackedSymbolRepository.findAll();
        for (TrackedSymbol s : symbols) {
            sentimentService.fetchAndSave(s.getSymbol());
        }
    }
}