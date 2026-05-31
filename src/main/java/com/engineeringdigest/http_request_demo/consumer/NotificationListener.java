package com.engineeringdigest.http_request_demo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {

    @KafkaListener(topics = "user-events", groupId = "notification-service")
    public void handleUserEvents(String message){
        log.info("[NOTIFICATION] Received event: {}", message);
        log.info("[NOTIFICATION] Sending welcome email...");
    }
}
