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
 * Core functionalities:
 * - Listens to Kafka messages containing stock notification data in a predefined format.
 * - Parses the incoming message data to extract stock symbol, sentiment, and count.
 * - Creates and saves a {@code Notification} entity with the parsed information
 *   including the timestamp and viewed status.
 *
 * Dependencies:
 * - {@code NotificationRepository}: Manages persistence and retrieval of notification data.
 *
 * Kafka Setup:
 * - Topic: stock-notifications
 * - Consumer Group ID: notification-consumer-group
 *
 * Error Handling:
 * - Logs and prints stack trace for any exception encountered while processing messages.
 *
 */
@Service
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;

    public NotificationConsumer(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Listens for Kafka messages from the "stock-notifications" topic and processes the data
     * to persist notification records in the database. The incoming message is expected to follow
     * the format "symbol:sentiment:count".
     *
     * If the message is parsed successfully:
     * - Extracts the stock symbol, sentiment, and count from the message.
     * - Creates a {@code Notification} instance with the extracted data, including the current timestamp.
     * - Saves the {@code Notification} in the database via {@code NotificationRepository}.
     *
     * In case of processing errors, stack traces are logged.
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