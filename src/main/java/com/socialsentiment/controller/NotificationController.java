package com.socialsentiment.controller;

import com.socialsentiment.entity.Notification;
import com.socialsentiment.repository.NotificationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// add import for slf4j logger via lombok
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j // enables us of log for structured logging 
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/api/notifications")
    public List<Notification> getUnreadNotifications() {
        // log the request for tracking
        log.info("Fetching unread notifications"); // do it before querying repository 
        return notificationRepository.findByViewedFalseOrderByTimestampDesc();
    }

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
