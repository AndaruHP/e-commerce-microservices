package com.ecommerce.inventoryservice.config;

import com.ecommerce.inventoryservice.event.OrderCancelledEvent;
import com.ecommerce.inventoryservice.event.OrderCreatedEvent;
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
    public NewTopic orderCreatedTopic() {
        return new NewTopic("order.created", 1, (short) 1);
    }

    @Bean
    public NewTopic orderCancelledTopic() {
        return new NewTopic("order.cancelled", 1, (short) 1);
    }

    @Bean
    public ConsumerFactory<String, OrderCreatedEvent> orderCreatedEventConsumerFactory(
            KafkaProperties properties
    ) {
        return new DefaultKafkaConsumerFactory<>(
                properties.buildConsumerProperties(),
                new StringDeserializer(),
                new JacksonJsonDeserializer<>(OrderCreatedEvent.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> orderCreatedEventConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, OrderCreatedEvent> orderCreatedEventConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderCreatedEventConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderCancelledEvent> orderCancelledEventConsumerFactory(
            KafkaProperties properties
    ) {
        return new DefaultKafkaConsumerFactory<>(
                properties.buildConsumerProperties(),
                new StringDeserializer(),
                new JacksonJsonDeserializer<>(OrderCancelledEvent.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCancelledEvent> orderCancelledEventConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, OrderCancelledEvent> orderCancelledEventConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, OrderCancelledEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderCancelledEventConsumerFactory);
        return factory;
    }
}
