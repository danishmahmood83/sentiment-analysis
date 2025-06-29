package com.socialsentiment.service;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CoreNlpSentimentAnalyzerTest {

    @Mock
    private StanfordCoreNLP mockPipeline;

    @Mock
    private CoreDocument mockDocument;

    @Mock
    private CoreSentence mockSentence;

    @InjectMocks
    private CoreNlpSentimentAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks before each test
    }

    @Test
    void analyzeSentiment_returnsBullishForPositive() {
        String message = "Good performance";
        String symbol = "AAPL";

        doAnswer(invocation -> {
            CoreDocument docArg = invocation.getArgument(0);

            // When docArg.sentences() called, return mocked sentence list
            when(docArg.sentences()).thenReturn(List.of(mockSentence));
            when(mockSentence.sentiment()).thenReturn("Positive");
            return null;
        }).when(mockPipeline).annotate(any(CoreDocument.class));

        String sentiment = analyzer.analyzeSentiment(message, symbol);
        assertThat(sentiment).isEqualTo("bullish");
    }

    @Test
    void analyzeSentiment_returnsBearishForNegative() {
        String message = "Bad news";
        String symbol = "TSLA";

        doAnswer(invocation -> {
            CoreDocument docArg = invocation.getArgument(0);
            when(docArg.sentences()).thenReturn(List.of(mockSentence));
            when(mockSentence.sentiment()).thenReturn("Negative");
            return null;
        }).when(mockPipeline).annotate(any(CoreDocument.class));

        String sentiment = analyzer.analyzeSentiment(message, symbol);
        assertThat(sentiment).isEqualTo("bearish");
    }

    @Test
    void analyzeSentiment_returnsNeutralIfNoSentences() {
        String message = "No data";
        String symbol = "GOOGL";

        doAnswer(invocation -> {
            CoreDocument docArg = invocation.getArgument(0);
            when(docArg.sentences()).thenReturn(List.of());
            return null;
        }).when(mockPipeline).annotate(any(CoreDocument.class));

        String sentiment = analyzer.analyzeSentiment(message, symbol);
        assertThat(sentiment).isEqualTo("neutral");
    }
}
