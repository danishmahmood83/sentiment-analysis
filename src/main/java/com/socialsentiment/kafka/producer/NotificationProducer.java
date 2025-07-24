package com.socialsentiment.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


/**
 * Service class responsible for producing and sending notification messages to a Kafka topic.
 */
@Service
public class NotificationProducer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class); // logger setup


    /**
     * KafkaTemplate instance used for producing and sending messages to a Kafka topic.
     * This is configured to work with String key and String value pairs.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * The name of the Kafka topic to which stock notification messages are sent.
     * This topic is used by the NotificationProducer service to publish messages.
     */
    private static final String TOPIC = "stock-notifications";

    /**
     * Constructor for NotificationProducer class.
     *
     * @param kafkaTemplate the KafkaTemplate used for sending messages to a Kafka topic
     */
    public NotificationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a notification message to the predefined Kafka topic.
     *
     * @param message the notification message to be sent to the Kafka topic
     */
    public void sendNotification(String message) {
        // log before sending 
        logger.info("Sending Kafka message to TOPIC: {}", TOPIC);
        // log raw message at debug level
        logger.debug("Message content {}", message);

        try {
            // send message to Kafka
            kafkaTemplate.send(TOPIC, message);
            // log before sending 
            logger.info("Kafka message sent successfully");
        } catch (Exception e) {
            // log any failure to send message
            logger.error("Failed to send Kafka message: {}", message, e);
        }
        
    }
}
