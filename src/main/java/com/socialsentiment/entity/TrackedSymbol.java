package com.socialsentiment.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Represents a tracked stock symbol, including its related metadata.
 * This entity is mapped to the "tracked_symbols" table in the database.
 *
 * Each tracked symbol has an identifier, a stock symbol, a name, and a
 * timestamp indicating when it was created. The creation timestamp is
 * automatically set when the entity is persisted.
 *
 * Fields:
 * - `id`: The unique identifier for the tracked symbol. It is generated automatically.
 * - `symbol`: The stock symbol being tracked (e.g., "AAPL" for Apple Inc.).
 * - `name`: The name associated with the tracked symbol (e.g., "Apple Inc.").
 * - `createdAt`: The timestamp when the entity was created, automatically set before persisting.
 *
 * Behavior:
 * - The `onCreate` method is triggered before the entity is persisted to set the `createdAt` field to the current timestamp.
 */
@Entity
@Table(name = "tracked_symbols")
public class TrackedSymbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String name;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    // Automatically set createdAt before saving
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }


}