package com.socialsentiment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FinBertSentimentAnalyzerTest {

    @Mock
    private HttpClient mockHttpClient;
    @Mock
    private HttpResponse<String> mockHttpResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private FinBertSentimentAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new FinBertSentimentAnalyzer(mockHttpClient,objectMapper);
    }

    @Test
    void analyzeSentimentWithFinBERT_ReturnsBullish() throws Exception {
        String jsonResponse = createMockResponse("positive", 0.9, "neutral", 0.05, "negative", 0.05);

        when(mockHttpClient.send(any(HttpRequest.class), (HttpResponse.BodyHandler<String>) any()))
                .thenReturn(mockHttpResponse);

        when(mockHttpResponse.body()).thenReturn(jsonResponse);

        String result = analyzer.analyzeSentimentWithFinBERT("AAPL", "Apple is doing well");

        assertEquals("bullish", result);
    }


    // other tests...

    private String createMockResponse(String label1, double score1, String label2, double score2, String label3, double score3) throws Exception {
        ArrayNode outerArray = objectMapper.createArrayNode();
        ArrayNode predictions = objectMapper.createArrayNode();

        ObjectNode p1 = objectMapper.createObjectNode();
        p1.put("label", label1);
        p1.put("score", score1);
        predictions.add(p1);

        ObjectNode p2 = objectMapper.createObjectNode();
        p2.put("label", label2);
        p2.put("score", score2);
        predictions.add(p2);

        ObjectNode p3 = objectMapper.createObjectNode();
        p3.put("label", label3);
        p3.put("score", score3);
        predictions.add(p3);

        outerArray.add(predictions);
        return objectMapper.writeValueAsString(outerArray);
    }
}
