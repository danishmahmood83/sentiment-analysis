package com.socialsentiment.scheduler;

import com.socialsentiment.entity.TrackedSymbol;
import com.socialsentiment.kafka.producer.NotificationProducer;
import com.socialsentiment.repository.StockSentimentRepository;
import com.socialsentiment.repository.TrackedSymbolRepository;
import com.socialsentiment.service.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Scheduler for regularly fetching and processing stock sentiment data.
 * This class uses Spring's {@code @Scheduled} annotation to execute a periodic task
 * that fetches sentiment information for tracked stock symbols and saves the data
 * to the database.
 *
 */
@Component
public class StockSentimentScheduler {
    @Autowired
    SentimentService sentimentService;
    @Autowired
    private TrackedSymbolRepository trackedSymbolRepository;
    @Autowired
    private NotificationProducer notificationProducer;
    @Autowired
    private StockSentimentRepository  stockSentimentRepository;


    //@Scheduled(fixedRate = 60000)
    /**
     * Periodically fetches sentiment data for all tracked stock symbols and processes it.
     *
     * This method is executed at a fixed rate of 60,000 milliseconds (1 minute) using
     * Spring's {@code @Scheduled} annotation. It performs the following actions:
     */
    @Scheduled(fixedRate = 60000)
    public void scheduledFetch() {
        List<TrackedSymbol> symbols = trackedSymbolRepository.findAll();
        for (TrackedSymbol s : symbols) {
            sentimentService.fetchAndSave(s.getSymbol());
            checkAndNotifyThreshold(s.getSymbol());

        }
    }

    /**
     * Checks the distribution of sentiment counts for a given stock symbol and sends
     * notifications if any sentiment type (bullish, bearish, or neutral) meets a threshold.
     *
     * @param symbol the stock symbol for which sentiment counts are analyzed
     */
    public void checkAndNotifyThreshold(String symbol) {
        // Query the repository to count sentiment by type for this symbol
        Map<String, Long> sentimentCounts = stockSentimentRepository.countBySymbolGroupBySentiment(symbol);

        long bullishCount = sentimentCounts.getOrDefault("bullish", 0L);
        long bearishCount = sentimentCounts.getOrDefault("bearish", 0L);
        long neutralCount = sentimentCounts.getOrDefault("neutral", 0L);

        long total = bullishCount + bearishCount + neutralCount;

        // Avoid division by zero
        if (total == 0) return;

        double bullishPct = (bullishCount * 100.0) / total;
        double bearishPct = (bearishCount * 100.0) / total;
        double neutralPct = (neutralCount * 100.0) / total;


        if (bullishCount >= 50) {
            String msg = symbol + ":bullish:" + bullishPct;
            notificationProducer.sendNotification(msg);
        }

        if (bearishCount >= 50) {
            String msg = symbol + ":bearish:" + bearishPct;
            notificationProducer.sendNotification(msg);
        }


        if (neutralCount >= 50) {
            String msg = symbol + ":neutral:" + neutralPct;
            notificationProducer.sendNotification(msg);
        }

    }
}