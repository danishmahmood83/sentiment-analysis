package com.socialsentiment.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.socialsentiment.service.FmpSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// add import for slf4j logger via lombok
import lombok.extern.slf4j.Slf4j;
/**
 * SymbolSearchController is a REST controller that provides an endpoint for searching
 * financial symbols using a query string. The search relies on the FmpSearchService to
 * fetch data from an external API.
 *
 * The controller maps requests to the path "/api/search". It includes functionality
 * for retrieving a list of financial symbols that match the provided search query.
 */
@Slf4j // enables us of log for structured logging 
@RestController
@RequestMapping("/api/search")

public class SymbolSearchController {

    @Autowired
    private final FmpSearchService searchService;

    public SymbolSearchController(FmpSearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Performs a search for symbols based on the provided query.
     * Delegates the functionality to the `searchService` to fetch symbol data.
     *
     * @param query the search query string used to find relevant symbols
     * @return a {@code JsonNode} containing the search results from the external service
     */
    @GetMapping
    public JsonNode search(@RequestParam String query) {
        log.info("Searching sentiments for symbol using query: {}", query); //log search query
        return searchService.searchSymbols(query);
    }
}
