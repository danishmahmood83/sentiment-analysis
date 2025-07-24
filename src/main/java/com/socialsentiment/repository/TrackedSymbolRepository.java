package com.socialsentiment.repository;

import com.socialsentiment.entity.TrackedSymbol;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing and accessing {@code TrackedSymbol} entities.
 * Extends the {@code JpaRepository} interface to provide basic CRUD operations
 * and custom query capabilities.
 */
public interface TrackedSymbolRepository extends JpaRepository<TrackedSymbol, String> {

    /**
     * Deletes a tracked symbol from the repository based on its symbol.
     *
     * @param symbol the stock symbol of the tracked entity to be deleted
     */
    @Transactional
    @Modifying
    @Query("delete from TrackedSymbol t where t.symbol = :symbol")
    void deleteBySymbol(String symbol);
}
