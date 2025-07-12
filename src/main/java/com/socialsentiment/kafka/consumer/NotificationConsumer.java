package com.socialsentiment.kafka.consumer;

import com.socialsentiment.entity.Notification;
import com.socialsentiment.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;

    public NotificationConsumer(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = "stock-notifications", groupId = "notification-consumer-group")
    public void listen(String message) {
        System.out.println("Received Kafka message: " + message);
        try {
            // Parse message, format: symbol:sentiment:count
            String[] parts = message.split(":");
            if (parts.length == 3) {
                String symbol = parts[0];
                String sentiment = parts[1];
                double percent = Double.parseDouble(parts[2]);

                Notification notification = new Notification();
                notification.setSymbol(symbol);
                notification.setSentiment(sentiment);
                notification.setPercent(percent);
                notification.setTimestamp(LocalDateTime.now());
                notification.setViewed(false);

                notificationRepository.save(notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}