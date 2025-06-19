package com.socialsentiment.controller;


import com.socialsentiment.entity.TrackedSymbol;
import com.socialsentiment.repository.TrackedSymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracked")
public class TrackedSymbolController {

    @Autowired
    private TrackedSymbolRepository repository;

    @GetMapping
    public List<TrackedSymbol> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public TrackedSymbol addSymbol(@RequestBody TrackedSymbol symbol) {
        return repository.save(symbol);
    }

    @DeleteMapping("/{symbol}")
    public void delete(@PathVariable String symbol) {
        repository.deleteBySymbol(symbol);
    }
}