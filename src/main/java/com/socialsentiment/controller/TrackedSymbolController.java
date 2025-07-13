package com.socialsentiment.controller;


import com.socialsentiment.entity.TrackedSymbol;
import com.socialsentiment.repository.TrackedSymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing tracked symbols via REST endpoints.
 * Provides functionalities to retrieve all tracked symbols, add a new symbol,
 * and delete a tracked symbol by its identifier.
 */
@RestController
@RequestMapping("/api/tracked")
public class TrackedSymbolController {

    /**
     * The {@code TrackedSymbolRepository} variable is an instance of the repository interface
     * responsible for performing database operations on {@code TrackedSymbol} entities.
     */
    @Autowired
    private TrackedSymbolRepository repository;

    /**
     * Retrieves a list of all tracked symbols from the repository.
     *
     * @return a list of {@code TrackedSymbol} objects representing all tracked symbols
     */
    @GetMapping
    public List<TrackedSymbol> getAll() {
        return repository.findAll();
    }

    /**
     * Adds a new tracked symbol to the repository.
     *
     * @param symbol the {@code TrackedSymbol} object representing the symbol to be added
     * @return the saved {@code TrackedSymbol} object after persisting it in the repository
     */
    @PostMapping
    public TrackedSymbol addSymbol(@RequestBody TrackedSymbol symbol) {
        return repository.save(symbol);
    }

    /**
     * Deletes a tracked symbol from the repository by its symbol.
     *
     * @param symbol the symbol of the tracked entity to delete
     */
    @DeleteMapping("/{symbol}")
    public void delete(@PathVariable String symbol) {
        repository.deleteBySymbol(symbol);
    }
}