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

@RestController
public class NotificationController {

    private final NotificationRepository notificationRepository;

    private final MeterRegistry meterRegistry;

    private final AtomicInteger unreadNotificationCount = new AtomicInteger(0);


    public NotificationController(NotificationRepository notificationRepository, MeterRegistry meterRegistry) {
        this.notificationRepository = notificationRepository;
        this.meterRegistry = meterRegistry;
        //  Register a gauge that reads from the AtomicInteger
        Gauge.builder("notifications_unread_count", unreadNotificationCount::get)
                .description("Current unread notifications count")
                .register(meterRegistry);
    }

    @GetMapping("/api/notifications")
    public List<Notification> getUnreadNotifications() {
        //  Custom metric increment
        List<Notification>  list=notificationRepository.findByViewedFalseOrderByTimestampDesc();
        unreadNotificationCount.set(list.size());
        return list;
    }

    @PostMapping("/api/notifications/{id}/viewed")
    public void markAsViewed(@PathVariable Long id) {
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setViewed(true);
            notificationRepository.save(notification);
        });
    }
}
