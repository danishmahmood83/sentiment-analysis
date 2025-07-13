package com.socialsentiment.repository;

import com.socialsentiment.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for managing and accessing {@code Notification} entities.
 * Extends the {@code JpaRepository} interface to provide CRUD operations and
 * custom query capabilities for the {@code Notification} entity.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Retrieves a list of notifications where the viewed status is set to false,
     * ordered by their timestamp in descending order.
     *
     * @return a list of {@code Notification} entities that have not been viewed,
     *         sorted by their timestamp in descending order
     */
    List<Notification> findByViewedFalseOrderByTimestampDesc();
}
