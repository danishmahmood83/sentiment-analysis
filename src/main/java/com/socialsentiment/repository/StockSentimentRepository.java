package com.socialsentiment.repository;

import com.socialsentiment.entity.StockSentiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Query("SELECT s.sentiment AS sentiment, COUNT(s) AS count FROM StockSentiment s WHERE s.symbol = :symbol GROUP BY s.sentiment")
    List<Object[]> countSentimentBySymbol(@Param("symbol") String symbol);

    default Map<String, Long> countBySymbolGroupBySentiment(String symbol) {
        List<Object[]> results = countSentimentBySymbol(symbol);
        return results.stream().collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (Long) row[1]
        ));
    }

}