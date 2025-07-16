package com.socialsentiment.kafka.consumer;

import com.socialsentiment.entity.Notification;
import com.socialsentiment.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * Service class responsible for consuming Kafka messages from the "stock-notifications" topic
 * and processing the data to persist notification records into a database.
 *
 * Dependencies:
 * - {@code NotificationRepository}: Manages persistence and retrieval of notification data.
 *
 */
@Service
public class NotificationConsumer {

    /**
     * Reference to the {@code NotificationRepository}, which is responsible for managing
     * and accessing {@code Notification} entities in the database.
     *
     * It enables the processing and persistence of notifications consumed by the
     * {@code NotificationConsumer} service from Kafka messages.
     */
    private final NotificationRepository notificationRepository;

    /**
     * Constructs a new {@code NotificationConsumer} instance with the specified
     * {@code NotificationRepository}.
     *
     * @param notificationRepository the repository used for managing and accessing
     *                                {@code Notification} entities in the database
     */
    public NotificationConsumer(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Listens for Kafka messages from the "stock-notifications" topic and processes the data
     * to persist notification records in the database. The incoming message is expected to follow
     * the format "symbol:sentiment:count".
     *
     * @param message the incoming Kafka message, expected in the format "symbol:sentiment:count".
     */
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