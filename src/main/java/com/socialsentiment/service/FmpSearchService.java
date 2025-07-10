
package com.socialsentiment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FmpSearchService {
    private static final Logger logger = LoggerFactory.getLogger(FmpSearchService.class);
    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode searchSymbols(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            String url = "https://financialmodelingprep.com/api/v3/search?query=" + encodedQuery +
                    "&limit=10&exchange=NASDAQ&apikey=" + "vJCczyyULNOmJWrLDJ8fFZhzXJZcqSbd";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readTree(response.body());

        } catch (Exception e) {
            logger.error("Error while searching symbols: {}", e.getMessage());
            return objectMapper.createArrayNode();
        }
    }
}