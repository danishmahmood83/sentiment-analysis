package com.socialsentiment.service;

import edu.stanford.nlp.pipeline.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;


/**
 * A service for performing sentiment analysis of text data using the Stanford CoreNLP library.
 *
 * This class leverages Stanford CoreNLP's sentiment analysis capabilities to evaluate the sentiment
 * of financial messages. The sentiment is categorized into three types: "bullish", "bearish", or "neutral",
 * based on the sentiment classification provided by the library.
 */
@Service
public class CoreNlpSentimentAnalyzer {

    private final StanfordCoreNLP pipeline;

    /**
     * Constructs a new instance of CoreNlpSentimentAnalyzer with a pre-configured StanfordCoreNLP pipeline.
     */
    public CoreNlpSentimentAnalyzer() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,sentiment");
        this.pipeline = new StanfordCoreNLP(props);
    }



    /**
     * Analyzes the sentiment of a financial message for a given symbol and categorizes it as
     * "bullish", "bearish", or "neutral".
     *
     * @param message The financial message to be analyzed for sentiment.
     * @param symbol The financial symbol associated with the message.
     * @return A string indicating the financial sentiment of the message:
     *         "bullish", "bearish", or "neutral".
     */
    public String analyzeSentiment(String message,String symbol) {
        String text = String.format("Financial sentiment for %s: %s", symbol, message);
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        if (document.sentences().isEmpty()) return "neutral";

        String sentiment = document.sentences().get(0).sentiment().toLowerCase();

        return switch (sentiment) {
            case "very positive", "positive" -> "bullish";
            case "very negative", "negative" -> "bearish";
            default -> "neutral";
        };
    }
}
