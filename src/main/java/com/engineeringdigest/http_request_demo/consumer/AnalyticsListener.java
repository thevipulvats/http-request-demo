package com.engineeringdigest.http_request_demo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AnalyticsListener {

    @KafkaListener(topics = "user-events", groupId = "analytics-service")
    public void handleUserEvents(ConsumerRecord<String, String> record){
        log.info("[ANALYTICS] key={} partition={} offset={} value={}",
                record.key(), record.partition(), record.offset(), record.value());
        log.info("[ANALYTICS] Recording signup metric...");
    }
}
