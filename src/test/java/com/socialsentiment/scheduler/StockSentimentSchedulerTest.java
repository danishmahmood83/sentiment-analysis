package com.socialsentiment.scheduler;

import com.socialsentiment.controller.TrackedSymbolController;
import com.socialsentiment.entity.TrackedSymbol;
import com.socialsentiment.repository.TrackedSymbolRepository;
import com.socialsentiment.service.SentimentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockSentimentSchedulerTest {

    @Mock
    private TrackedSymbolRepository repo;

    @Mock
    private SentimentService service;

    @InjectMocks
    private StockSentimentScheduler scheduler;

    @Test
    void testScheduledFetch() {

        TrackedSymbol symbol = new TrackedSymbol();
        symbol.setSymbol("GOOG");
        when(repo.findAll()).thenReturn(List.of(symbol));
        scheduler.scheduledFetch();
        verify(service).fetchAndSave("GOOG");
    }
}
