package com.socialsentiment.repository;

import com.socialsentiment.entity.TrackedSymbol;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TrackedSymbolRepository extends JpaRepository<TrackedSymbol, String> {

    @Transactional
    @Modifying
    @Query("delete from TrackedSymbol t where t.symbol = :symbol")
    void deleteBySymbol(String symbol);
}
