package com.engineeringdigest.http_request_demo.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userEventsTopic(){
        return TopicBuilder.name("user-events").partitions(3).replicas(1).build();
    }
}
