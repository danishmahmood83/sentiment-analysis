
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


/**
 * Service responsible for interacting with the Financial Modeling Prep (FMP) API
 * to search for stock symbols based on a given query. The service communicates with
 * an external API, parses the response, and returns the result in a structured format.
 */
@Service
public class FmpSearchService {
    private static final Logger logger = LoggerFactory.getLogger(FmpSearchService.class);
    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Searches for stock symbols based on the provided query using the Financial Modeling Prep API.
     *
     * @param query The search term used to query stock symbols. It is expected to be a keyword or partial text
     *              representing the stock ticker or company name.
     * @return A JsonNode containing the search results from the API in a structured format. Returns an empty
     *         JsonNode if an error occurs during the API call or processing.
     */
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