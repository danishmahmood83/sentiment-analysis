package com.socialsentiment.scheduler;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.socialsentiment.entity.TrackedSymbol;
import com.socialsentiment.kafka.producer.NotificationProducer;
import com.socialsentiment.repository.StockSentimentRepository;
import com.socialsentiment.repository.TrackedSymbolRepository;
import com.socialsentiment.service.SentimentService;
/**
 * Scheduler for regularly fetching and processing stock sentiment data.
 * This class uses Spring's {@code @Scheduled} annotation to execute a periodic task
 * that fetches sentiment information for tracked stock symbols and saves the data
 * to the database.
 *
 */
@Component
public class StockSentimentScheduler {
    /**
     * An instance of {@code SentimentService} to manage the process of fetching,
     * analyzing, and storing stock sentiment data.
     */

    /**
     * SLF4J logger for this class - standard logging interface
     */
    private static final Logger logger = LoggerFactory.getLogger(StockSentimentScheduler.class); // logger setup
    @Autowired
    SentimentService sentimentService;
    /**
     * Repository for managing and accessing {@code TrackedSymbol} entities.
     */
    @Autowired
    private TrackedSymbolRepository trackedSymbolRepository;
    /**
     * A component responsible for producing and sending notification messages to Kafka.
     */
    @Autowired
    private NotificationProducer notificationProducer;
    /**
     * Repository for managing stock sentiment data persistence in the scheduling service.
     */
    @Autowired
    private StockSentimentRepository  stockSentimentRepository;




    //@Scheduled(fixedRate = 60000)
    /**
     * Periodically fetches sentiment data for all tracked stock symbols and processes it.
     *
     * This method is executed at a fixed rate of 60,000 milliseconds (1 minute) using
     * Spring's {@code @Scheduled} annotation. It performs the following actions:
     */
    @Scheduled(fixedRate = 300000)

    public void scheduledFetch() {
        logger.info("-------------------------------------"); // log scheduler starts
        logger.info("Scheduled fetch triggered trackSymbols"); // log scheduler starts
        logger.info("-------------------------------------"); // log scheduler starts

        logger.info("Running scheduled task to track symbol "); // log scheduler starts
        List<TrackedSymbol> symbols = trackedSymbolRepository.findAll();
        
        for (TrackedSymbol s : symbols) {
            logger.debug("Processing symbol:{} ", s.getSymbol());// logs which symbol is being processed
            try {
                sentimentService.fetchAndSave(s.getSymbol()); // runs sentiment fetch core logic 
                checkAndNotifyThreshold(s.getSymbol()); // triggers / checks notifications if needed
                
            } catch (Exception e) {
                logger.error("ERROR processing symbol{} in scheduled task", s.getSymbol()); // catches and logs all exception errors with stack trace
            }
            
        }
        logger.info("Scheduler completed processing");// log end of scheduled task
    }

    /**
     * Checks the distribution of sentiment counts for a given stock symbol and sends
     * notifications if any sentiment type (bullish, bearish, or neutral) meets a threshold.
     *
     * @param symbol the stock symbol for which sentiment counts are analyzed
     */
    public void checkAndNotifyThreshold(String symbol) {
        try {
            // Query the repository to count sentiment by type for this symbol
            Map<String, Long> sentimentCounts = stockSentimentRepository.countBySymbolGroupBySentiment(symbol);

            long bullishCount = sentimentCounts.getOrDefault("bullish", 0L);
            long bearishCount = sentimentCounts.getOrDefault("bearish", 0L);
            long neutralCount = sentimentCounts.getOrDefault("neutral", 0L);

            long total = bullishCount + bearishCount + neutralCount;

             // Avoid division by zero
            if (total == 0) {
                logger.warn("No sentiment data for symbol: {}", symbol); // avoids a silent skip 
                return;
            }

            double bullishPct = (bullishCount * 100.0) / total;
            double bearishPct = (bearishCount * 100.0) / total;
            double neutralPct = (neutralCount * 100.0) / total;

            // logs breakdown of sentiments for traceability 
            logger.debug("Sentiment distribution for {} - Bullish: {}%, Neutral: {}%", symbol, bullishPct, bearishPct, neutralPct);
            // logs breakdown of sentiments for traceability 
            logger.debug("Sentiment distribution for {} - Bullish: {}%, Neutral: {}%", symbol, bullishPct, bearishPct, neutralPct);

            if (bullishPct >= 50) {
                String msg = symbol + ":bullish:" + bullishPct;
                notificationProducer.sendNotification(msg);
                logger.info("Notification Sent", msg); // log when notification is sent
            }

            if (bearishPct >= 50) {
                String msg = symbol + ":bearish:" + bearishPct;
                notificationProducer.sendNotification(msg);
                logger.info("Notification Sent", msg); // log when notification is sent
            }

            if (neutralPct >= 50) {
                String msg = symbol + ":neutral:" + neutralPct;
                notificationProducer.sendNotification(msg);
                logger.info("Notification Sent", msg); // log when notification is sent
            }

        } catch (Exception e) {
            logger.error("Error in check and NotifyThreshold() for symbol: {}", symbol, e);// catches and logs all exception errors with stack trace
        }
        

    }
}