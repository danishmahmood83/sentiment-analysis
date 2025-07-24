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
 * Responsibilities:
 * - Retrieve all tracked stock symbols from the repository.
 * - For each tracked symbol, invoke the sentiment service to fetch and analyze
 *   sentiment data.
 * - Persist the processed data into the sentiment repository.
 *
 * Dependencies:
 * - {@code SentimentService}: Handles fetching and sentiment analysis logic.
 * - {@code TrackedSymbolRepository}: Provides access to the list of tracked stock symbols.
 *
 * Scheduling:
 * - The task is executed periodically with a fixed rate of 60,000 milliseconds (1 minute).
 *
 * Annotations:
 * - {@code @Component}: Marks this class as a Spring-managed component.
 * - {@code @Autowired}: Injects dependencies automatically.
 * - {@code @Scheduled}: Defines the periodic schedule for the execution of tasks.
 */
@Component
public class StockSentimentScheduler {

    /**
     * SLF4J logger for this class - standard logging interface
     */
    private static final Logger logger = LoggerFactory.getLogger(StockSentimentScheduler.class); // logger setup
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
     *
     * 1. Retrieves the list of all tracked stock symbols from the {@code TrackedSymbolRepository}.
     * 2. Iterates over the retrieved symbols.
     * 3. Invokes the {@code fetchAndSave} method of the {@code SentimentService} for each symbol.
     *
     * The {@code SentimentService} handles the fetching of sentiment data from an external API,
     * performs sentiment analysis, and persists the results into the database.
     *
     * Dependencies:
     * - {@code TrackedSymbolRepository}: Provides access to the list of tracked stock symbols.
     * - {@code SentimentService}: Handles fetching, sentiment analysis, and data persistence.
     *
     * Scheduling:
     * - The method is executed periodically with a fixed delay of 60,000 milliseconds (1 minute).
     *
     * Annotation:
     * - {@code @Scheduled}: Specifies the task scheduler's fixed interval for execution.
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

    /*
     * Checks the sentiment percentages for a symbol and sends Kafak notification if any passes 50%
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