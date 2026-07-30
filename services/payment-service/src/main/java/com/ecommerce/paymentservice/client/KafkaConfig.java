package com.ecommerce.paymentservice.client;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic paymentCompletedTopic() {
        return new NewTopic("payment.completed", 1, (short) 1);
    }
}
