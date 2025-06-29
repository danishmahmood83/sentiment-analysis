package com.socialsentiment.service;

import edu.stanford.nlp.pipeline.*;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class CoreNlpSentimentAnalyzer {

    private final StanfordCoreNLP pipeline;

    public CoreNlpSentimentAnalyzer(StanfordCoreNLP mockPipeline) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,sentiment");
        this.pipeline = new StanfordCoreNLP(props);
    }

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
