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
 *
 * Key responsibilities:
 * - Persist, retrieve, update, and delete tracked symbol data.
 * - Provide a method to delete a tracked symbol by its symbol field.
 *
 * Methods:
 * - {@code deleteBySymbol(String symbol)}: Deletes a tracked symbol from the repository
 *   based on its symbol. This method modifies the database by executing a
 *   custom delete query.
 */
public interface TrackedSymbolRepository extends JpaRepository<TrackedSymbol, String> {

    @Transactional
    @Modifying
    @Query("delete from TrackedSymbol t where t.symbol = :symbol")
    void deleteBySymbol(String symbol);
}
