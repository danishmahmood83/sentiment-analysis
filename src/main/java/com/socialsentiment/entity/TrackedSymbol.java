package com.socialsentiment.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Represents a tracked stock symbol, including its related metadata.
 * This entity is mapped to the "tracked_symbols" table in the database.
 *
 **/
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