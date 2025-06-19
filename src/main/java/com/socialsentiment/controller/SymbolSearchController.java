package com.socialsentiment.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.socialsentiment.service.FmpSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SymbolSearchController {

    @Autowired
    private FmpSearchService searchService;

    @GetMapping
    public JsonNode search(@RequestParam String query) {
        return searchService.searchSymbols(query);
    }
}
