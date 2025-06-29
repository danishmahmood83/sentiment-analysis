package com.socialsentiment.service;

import com.fasterxml.jackson.databind.JsonNode;
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
class FmpSearchServiceTest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private FmpSearchService fmpSearchService;

    @BeforeEach
    void setUp() {
        fmpSearchService = new FmpSearchService(mockHttpClient, objectMapper);
    }

    @Test
    void testSearchSymbols_ReturnsJsonArray() throws Exception {
        String jsonResponse = """
                [
                  {"symbol":"AAPL","name":"Apple Inc.","exchange":"NASDAQ"},
                  {"symbol":"GOOGL","name":"Alphabet Inc.","exchange":"NASDAQ"}
                ]
                """;

        when(mockHttpClient.send(any(HttpRequest.class), (HttpResponse.BodyHandler<String>) any()))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(jsonResponse);

        JsonNode result = fmpSearchService.searchSymbols("apple");

        assertNotNull(result);
        assertTrue(result.isArray());
        assertEquals(2, result.size());
        assertEquals("AAPL", result.get(0).get("symbol").asText());
    }

    @Test
    void testSearchSymbols_HandlesEmptyResponse() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), (HttpResponse.BodyHandler<String>) any()))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn("[]");

        JsonNode result = fmpSearchService.searchSymbols("xyz");

        assertNotNull(result);
        assertTrue(result.isArray());
        assertEquals(0, result.size());
    }

    @Test
    void testSearchSymbols_HandlesInvalidJson() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), (HttpResponse.BodyHandler<String>) any()))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn("INVALID_JSON");

        JsonNode result = fmpSearchService.searchSymbols("invalid");

        assertNotNull(result);
        assertTrue(result.isArray()); // fallback arrayNode
        assertEquals(0, result.size());
    }

    @Test
    void testSearchSymbols_HandlesException() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), (HttpResponse.BodyHandler<String>) any()))
                .thenThrow(new RuntimeException("Network error"));

        JsonNode result = fmpSearchService.searchSymbols("fail");

        assertNotNull(result);
        assertTrue(result.isArray()); // fallback
        assertEquals(0, result.size());
    }
}
