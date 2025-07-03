package com.socialsentiment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The StockSentimentApplication is the entry point for the Stock Sentiment application.
 * It is a Spring Boot application that uses scheduling support.
 *
 * Use this class to bootstrap and launch the Stock Sentiment application.
 *
 * Analyzes stock sentiments and performs scheduled tasks as configured in the application.
 */
@SpringBootApplication
@EnableScheduling
public class StockSentimentApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockSentimentApplication.class, args);
    }
}

