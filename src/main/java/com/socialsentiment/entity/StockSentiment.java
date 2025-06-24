package com.socialsentiment.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_sentiment")
public class StockSentiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    @Lob
    private String message;

    private String sentiment;

    @Column(name = "message_id", unique = true)
    private Long messageId;

    private LocalDateTime createdAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSentiment() { return sentiment; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }

}
