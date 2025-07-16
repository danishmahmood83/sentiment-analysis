package com.socialsentiment.controller;

import com.socialsentiment.entity.Notification;
import com.socialsentiment.repository.NotificationRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// add import for slf4j logger via lombok
import lombok.extern.slf4j.Slf4j;

/**
 * Controller that provides endpoints for managing and interacting with notifications.
 *
 * Dependencies:
 * - `NotificationRepository`: Used for database operations related to notifications, such as fetching
 *   unread notifications and saving changes to a notification's state.
 */
@RestController
@Slf4j // enables us of log for structured logging
public class NotificationController {

    /**
     * The {@code notificationRepository} is a final instance of {@code NotificationRepository}
     * used to interact with the persistence layer for managing and retrieving notification entities.
     */
    private final NotificationRepository notificationRepository;

    private final MeterRegistry meterRegistry;

    private final AtomicInteger unreadNotificationCount = new AtomicInteger(0);

    /**
     * Constructs a new instance of the NotificationController.
     *
     * @param notificationRepository the NotificationRepository used for performing
     *                               database operations on notifications
     */
    public NotificationController(NotificationRepository notificationRepository, MeterRegistry meterRegistry) {
        this.notificationRepository = notificationRepository;
        this.meterRegistry = meterRegistry;
        //  Register a gauge that reads from the AtomicInteger
        Gauge.builder("notifications_unread_count", unreadNotificationCount::get)
                .description("Current unread notifications count")
                .register(meterRegistry);
    }

    /**
     * Retrieves a list of unread notifications, sorted in descending order by their timestamps.
     * A notification is considered unread if its `viewed` field is set to `false`.
     *
     * @return a list of unread notifications in descending order of timestamp
     */
    @GetMapping("/api/notifications")
    public List<Notification> getUnreadNotifications() {
        // log the request for tracking
        log.info("Fetching unread notifications"); // do it before querying repository
        List<Notification>  list=notificationRepository.findByViewedFalseOrderByTimestampDesc();
        unreadNotificationCount.set(list.size());
        return list;
    }

    /**
     * Marks a specific notification as viewed by updating its `viewed` flag to `true`.
     * The notification is identified via its unique ID and updated in the database.
     *
     * @param id the unique identifier of the notification to mark as viewed
     */
    @PostMapping("/api/notifications/{id}/viewed")
    public void markAsViewed(@PathVariable Long id) {
        // log the id that is being updated
        log.info("Marking notification {} as viewed" , id);
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setViewed(true);
            notificationRepository.save(notification);
            log.debug("Notification {} marked as viewed" , id); // log after saved
        });
    }
}
