package com.socialsentiment.controller;

import com.socialsentiment.entity.Notification;
import com.socialsentiment.repository.NotificationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller that provides endpoints for managing and interacting with notifications.
 *
 * Dependencies:
 * - `NotificationRepository`: Used for database operations related to notifications, such as fetching
 *   unread notifications and saving changes to a notification's state.
 */
@RestController
public class NotificationController {

    /**
     * The {@code notificationRepository} is a final instance of {@code NotificationRepository}
     * used to interact with the persistence layer for managing and retrieving notification entities.
     */
    private final NotificationRepository notificationRepository;

    /**
     * Constructs a new instance of the NotificationController.
     *
     * @param notificationRepository the NotificationRepository used for performing
     *                               database operations on notifications
     */
    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Retrieves a list of unread notifications, sorted in descending order by their timestamps.
     * A notification is considered unread if its `viewed` field is set to `false`.
     *
     * @return a list of unread notifications in descending order of timestamp
     */
    @GetMapping("/api/notifications")
    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByViewedFalseOrderByTimestampDesc();
    }

    /**
     * Marks a specific notification as viewed by updating its `viewed` flag to `true`.
     * The notification is identified via its unique ID and updated in the database.
     *
     * @param id the unique identifier of the notification to mark as viewed
     */
    @PostMapping("/api/notifications/{id}/viewed")
    public void markAsViewed(@PathVariable Long id) {
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setViewed(true);
            notificationRepository.save(notification);
        });
    }
}
