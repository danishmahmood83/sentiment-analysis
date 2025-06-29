package com.socialsentiment.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.socialsentiment.service.FmpSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SymbolSearchControllerTest {

    @Mock
    private FmpSearchService searchService;

    @InjectMocks
    private SymbolSearchController controller;

    @Test
    void testSearch() {
        JsonNode mockNode = mock(JsonNode.class);
        when(searchService.searchSymbols("AAPL")).thenReturn(mockNode);

        JsonNode result = controller.search("AAPL");
        assertEquals(mockNode, result);
        verify(searchService).searchSymbols("AAPL");
    }
}
