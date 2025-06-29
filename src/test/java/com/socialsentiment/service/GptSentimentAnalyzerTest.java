package com.socialsentiment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GptSentimentAnalyzerTest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    private ObjectMapper objectMapper = new ObjectMapper();
    private GptSentimentAnalyzer gptSentimentAnalyzer;

    @BeforeEach
    void setUp() {
        gptSentimentAnalyzer = new GptSentimentAnalyzer(mockHttpClient, objectMapper);
    }

    @Test
    void testAnalyzeSentimentWithGPT_ReturnsBullish() throws Exception {
        String responseJson = """
        {
          "choices": [
            {
              "message": {
                "content": "Bullish"
              }
            }
          ]
        }
        """;

        when(mockHttpClient.send(any(HttpRequest.class), (HttpResponse.BodyHandler<String>) any()))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(responseJson);

        String result = gptSentimentAnalyzer.analyzeSentimentWithGPT("Apple is going up", "AAPL");
        assertEquals("bullish", result);
    }

    @Test
    void testAnalyzeSentimentWithGPT_ReturnsBearish() throws Exception {
        String responseJson = """
        {
          "choices": [
            {
              "message": {
                "content": "Bearish"
              }
            }
          ]
        }
        """;

        when(mockHttpClient.send(any(HttpRequest.class), (HttpResponse.BodyHandler<String>) any()))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(responseJson);

        String result = gptSentimentAnalyzer.analyzeSentimentWithGPT("Apple might crash", "AAPL");
        assertEquals("bearish", result);
    }

    @Test
    void testAnalyzeSentimentWithGPT_ReturnsNeutral() throws Exception {
        String responseJson = """
        {
          "choices": [
            {
              "message": {
                "content": "Neutral"
              }
            }
          ]
        }
        """;

        when(mockHttpClient.send(any(HttpRequest.class), (HttpResponse.BodyHandler<String>) any()))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(responseJson);

        String result = gptSentimentAnalyzer.analyzeSentimentWithGPT("Apple released its earnings", "AAPL");
        assertEquals("neutral", result);
    }

    @Test
    void testAnalyzeSentimentWithGPT_WhenException_ReturnsNeutral() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), (HttpResponse.BodyHandler<String>) any()))
                .thenThrow(new RuntimeException("API error"));

        String result = gptSentimentAnalyzer.analyzeSentimentWithGPT("Error scenario", "AAPL");
        assertEquals("neutral", result);
    }

    @Test
    void testAnalyzeSentimentWithGPT_WhenContentUnexpected_ReturnsNeutral() throws Exception {
        String responseJson = """
        {
          "choices": [
            {
              "message": {
                "content": "Some unrelated text"
              }
            }
          ]
        }
        """;

        when(mockHttpClient.send(any(HttpRequest.class), (HttpResponse.BodyHandler<String>) any()))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(responseJson);

        String result = gptSentimentAnalyzer.analyzeSentimentWithGPT("Ambiguous result", "AAPL");
        assertEquals("neutral", result);
    }
}
