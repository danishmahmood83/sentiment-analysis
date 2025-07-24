package com.socialsentiment.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class); // logger setup


    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "stock-notifications";

    public NotificationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

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
