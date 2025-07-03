package com.socialsentiment.repository;

import com.socialsentiment.entity.StockSentiment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for managing and accessing {@code StockSentiment} entities.
 * Provides methods for retrieving and verifying stock sentiment data based on
 * specific attributes. Extends the {@code JpaRepository} interface to leverage
 * basic CRUD operations and query capabilities.
 *
 * Key responsibilities:
 * - Retrieve sentiments based on stock symbols.
 * - Check existence of sentiments by unique message IDs.
 */
public interface StockSentimentRepository extends JpaRepository<StockSentiment, Long> {
    List<StockSentiment> findBySymbol(String symbol);
    boolean existsByMessageIdAndAnalysisMethod(long messageId, String analysisMethod);
    List<StockSentiment> findBySymbolAndAnalysisMethod(String symbol, String analysisMethod);

}