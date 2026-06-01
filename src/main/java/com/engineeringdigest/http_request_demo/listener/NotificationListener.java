package com.engineeringdigest.http_request_demo.listener;


import com.engineeringdigest.http_request_demo.configuration.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATIONS_QUEUE)
    public void handleUserCreated(String message){
        log.info("[NOTIFICATION] Received: {}", message);
        log.info("[NOTIFICATION] Sending welcome email...");
    }
}
