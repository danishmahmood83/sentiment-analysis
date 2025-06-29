package com.socialsentiment.controller;

import com.socialsentiment.entity.StockSentiment;
import com.socialsentiment.repository.StockSentimentRepository;
import com.socialsentiment.service.SentimentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class SentimentControllerTest {

    @Mock
    private StockSentimentRepository repository;

    @Mock
    private SentimentService service;

    @InjectMocks
    private SentimentController controller;

    SentimentControllerTest() {
        openMocks(this);
    }

    @Test
    void testGetSentiments() {
        when(repository.findBySymbol("AAPL")).thenReturn(Collections.emptyList());
        List<StockSentiment> result = controller.getSentiments("AAPL");
        assertTrue(result.isEmpty());
    }

    @Test
    void testFetchAndSaveNow() {
        ResponseEntity<String> response = controller.fetchAndSaveNow("AAPL");
        verify(service).fetchAndSave("AAPL");
        assertEquals("Fetched and saved sentiment for: AAPL", response.getBody());
    }
}
