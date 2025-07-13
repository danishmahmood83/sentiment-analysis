package com.socialsentiment.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


/**
 * Represents a notification entity that stores information regarding stock-related notifications.
 * This class maps to the "notifications" table in the database and is managed by JPA for persistence.
 *
 * Fields:
 * - `id`: The unique identifier for the notification, generated automatically.
 * - `symbol`: The stock symbol associated with the notification (e.g., "AAPL" for Apple Inc.).
 * - `sentiment`: The sentiment conveyed by the notification (e.g., "positive" or "negative").
 * - `count`: The count or quantity associated with the notification, typically representing
 *   a numerical value linked to the sentiment or symbol.
 * - `timestamp`: The date and time when the notification was created, defaults to the current time.
 * - `viewed`: A boolean flag indicating whether the notification has been viewed or not.
 *   Defaults to false.
 *
 * Behavior:
 * - Provides getters and setters for all fields to support retrieval and updating of data.
 * - Automatically assigns the current timestamp to `timestamp` upon creation.
 *
 * An instance of this entity can be used to persist or manage notifications within a
 * database for tracking stock-related updates.
 */
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String sentiment;
    private double percent;
    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean viewed = false;

    // constructors/getters/setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
