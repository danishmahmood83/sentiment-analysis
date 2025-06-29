package com.socialsentiment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialsentiment.entity.StockSentiment;
import com.socialsentiment.repository.StockSentimentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.*;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SentimentServiceTest {

    @InjectMocks
    private SentimentService sentimentService;

    @Mock
    private StockSentimentRepository repository;
    @Mock
    private GptSentimentAnalyzer gptSentimentAnalyzer;
    @Mock
    private CoreNlpSentimentAnalyzer coreNlpSentimentAnalyzer;
    @Mock
    private FinBertSentimentAnalyzer finBertSentimentAnalyzer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String testSymbol = "AAPL";
    private final long messageId = 123456L;
    private final String messageText = "Apple stock is rising!";

    private String createMockApiResponse() {
        return """
            {
              "messages": [
                {
                  "id": 123456,
                  "body": "Apple stock is rising!",
                  "created_at": "2024-06-29T00:00:00Z"
                }
              ]
            }
        """;
    }

    @BeforeEach
    void setup() throws Exception {
        // Replace real HttpClient with mock using reflection (optional advanced)
        HttpClient mockHttpClient = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockResponse.body()).thenReturn(createMockApiResponse());
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Inject mockHttpClient using reflection
        var httpClientField = SentimentService.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        httpClientField.set(sentimentService, mockHttpClient);
    }

    @Test
    void testFetchAndSave_WhenNoSentimentExists_SavesAll() throws Exception {
        // Mock repository
        when(repository.existsByMessageIdAndAnalysisMethod(messageId, "gpt")).thenReturn(false);
        when(repository.existsByMessageIdAndAnalysisMethod(messageId, "stanford")).thenReturn(false);
        when(repository.existsByMessageIdAndAnalysisMethod(messageId, "finbert")).thenReturn(false);

        // Mock analyzers
        when(gptSentimentAnalyzer.analyzeSentimentWithGPT(messageText, testSymbol)).thenReturn("positive");
        when(coreNlpSentimentAnalyzer.analyzeSentiment(messageText, testSymbol)).thenReturn("neutral");
        when(finBertSentimentAnalyzer.analyzeSentimentWithFinBERT(testSymbol, messageText)).thenReturn("positive");

        // Run
        sentimentService.fetchAndSave(testSymbol);

        // Verify save
        verify(repository, times(1)).save(argThat((StockSentiment s) ->
                s.getAnalysisMethod().equals("gpt") &&
                        s.getSymbol().equals(testSymbol) &&
                        s.getMessageId() == messageId
        ));

        verify(repository, times(1)).save(argThat((StockSentiment s) ->
                s.getAnalysisMethod().equals("stanford")
        ));

        verify(repository, times(1)).save(argThat((StockSentiment s) ->
                s.getAnalysisMethod().equals("finbert")
        ));
    }

    @Test
    void testFetchAndSave_WhenAllExist_SavesNothing() throws Exception {
        when(repository.existsByMessageIdAndAnalysisMethod(anyLong(), eq("gpt"))).thenReturn(true);
        when(repository.existsByMessageIdAndAnalysisMethod(anyLong(), eq("stanford"))).thenReturn(true);
        when(repository.existsByMessageIdAndAnalysisMethod(anyLong(), eq("finbert"))).thenReturn(true);

        sentimentService.fetchAndSave(testSymbol);

        verify(repository, never()).save(any());
        verifyNoInteractions(gptSentimentAnalyzer, coreNlpSentimentAnalyzer, finBertSentimentAnalyzer);
    }
}
