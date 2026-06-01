package com.engineeringdigest.http_request_demo.listener;

import com.engineeringdigest.http_request_demo.configuration.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AnalyticsListener {

    @RabbitListener(queues = RabbitMQConfig.ANALYTICS_QUEUE)
    public void handleUserEvent(String message) {
        log.info("[ANALYTICS] Received: {}", message);
        log.info("[ANALYTICS] Recording signup metric...");
    }
}
