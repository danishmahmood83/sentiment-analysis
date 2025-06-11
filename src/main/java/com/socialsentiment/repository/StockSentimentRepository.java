package com.socialsentiment.repository;

import com.socialsentiment.entity.StockSentiment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockSentimentRepository extends JpaRepository<StockSentiment, Long> {
    List<StockSentiment> findBySymbol(String symbol);
}