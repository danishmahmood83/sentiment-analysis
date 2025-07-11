package com.socialsentiment.repository;

import com.socialsentiment.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for managing and accessing {@code Notification} entities.
 * Extends the {@code JpaRepository} interface to provide CRUD operations and
 * custom query capabilities for the {@code Notification} entity.
 *
 * Key responsibilities:
 * - Persist, retrieve, update, and delete notification data.
 * - Retrieve a list of notifications that have not been viewed, ordered by
 *   their timestamp in descending order.
 *
 * Methods:
 * - {@code List<Notification> findByViewedFalseOrderByTimestampDesc()}:
 *   Finds all notifications where the {@code viewed} property is false,
 *   and orders the results by the {@code timestamp} property in descending order.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByViewedFalseOrderByTimestampDesc();
}
