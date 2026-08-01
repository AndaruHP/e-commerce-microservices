package com.ecommerce.shippingservice.config;

import com.ecommerce.shippingservice.event.OrderConfirmedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic orderConfirmedTopic() {
        return new NewTopic("order.confirmed", 1, (short) 1);
    }

    @Bean
    public ConsumerFactory<String, OrderConfirmedEvent> orderConfirmedEventConsumerFactory(
            KafkaProperties properties
    ) {
        return new DefaultKafkaConsumerFactory<>(
                properties.buildConsumerProperties(),
                new StringDeserializer(),
                new JacksonJsonDeserializer<>(OrderConfirmedEvent.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderConfirmedEvent> orderConfirmedEventConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, OrderConfirmedEvent> orderConfirmedEventConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, OrderConfirmedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConfirmedEventConsumerFactory);
        return factory;
    }
}
