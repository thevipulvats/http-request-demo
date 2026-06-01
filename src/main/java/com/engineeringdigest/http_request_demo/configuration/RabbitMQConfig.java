package com.engineeringdigest.http_request_demo.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "user.exchange";
    public static final String NOTIFICATIONS_QUEUE = "user.notifications.queue";
    public static final String ANALYTICS_QUEUE = "user.analytics.queue";


    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue notificationsQueue() {
        return new Queue(NOTIFICATIONS_QUEUE, true);
    }


    @Bean
    public Queue analyticsQueue() {
        return new Queue(ANALYTICS_QUEUE, true);
    }


    @Bean
    public Binding notificationsBinding() {
        return BindingBuilder.bind(notificationsQueue())
                .to(userExchange())
                .with("user.created");
    }


    @Bean
    public Binding analysticsBinding() {
        return BindingBuilder.bind(analyticsQueue())
                .to(userExchange())
                .with("user.#");
    }

}
