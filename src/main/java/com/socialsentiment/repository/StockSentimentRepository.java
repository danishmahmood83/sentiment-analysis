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

    /**
     * Retrieves a list of stock sentiment data based on the provided stock symbol and analysis method.
     *
     * @param symbol the stock symbol for which sentiment data is to be retrieved
     * @param analysisMethod the analysis method used to filter sentiment data
     * @return a list of {@code StockSentiment} objects matching the given symbol and analysis method
     */
    List<StockSentiment> findBySymbolAndAnalysisMethod(String symbol, String analysisMethod);

    /**
     * Counts the occurrences of each sentiment category (e.g., positive, neutral, negative)
     * for a given stock symbol by querying the database. The result is grouped by sentiment type.
     *
     * @param symbol the stock symbol for which sentiment counts are to be calculated
     * @return a list of objects where each object is an array of two elements:
     *         the first element is the sentiment type (String),
     *         and the second element is the count of occurrences (Long) for that sentiment
     */
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