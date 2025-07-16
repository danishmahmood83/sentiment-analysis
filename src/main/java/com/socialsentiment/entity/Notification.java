package com.socialsentiment.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


/**
 * Represents a notification entity that stores information regarding stock-related notifications.
 *
 * This class maps to the "notifications" table in the database and is managed by JPA for persistence.
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

    /**
     * Retrieves the unique identifier of the entity.
     *
     * @return the unique ID of the entity as a {@code Long}
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the entity.
     *
     * @param id the unique ID of the entity as a {@code Long}
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the stock symbol associated with this entity.
     *
     * @return the stock symbol as a {@code String}
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the stock symbol associated with this notification.
     *
     * @param symbol the stock symbol as a {@code String}
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Retrieves the sentiment associated with the notification.
     *
     * @return the sentiment as a {@code String}
     */
    public String getSentiment() {
        return sentiment;
    }

    /**
     * Sets the sentiment value for the notification.
     *
     * The sentiment represents the interpreted emotional state or attitude
     * associated with the notification, such as "positive", "negative", or "neutral".
     *
     * @param sentiment the sentiment value as a {@code String}
     */
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
