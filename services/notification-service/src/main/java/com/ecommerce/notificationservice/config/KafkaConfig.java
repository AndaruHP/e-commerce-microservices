package com.ecommerce.notificationservice.config;

import com.ecommerce.notificationservice.event.OrderCreatedEvent;
import com.ecommerce.notificationservice.event.PaymentCompletedEvent;
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
    public NewTopic paymentCompletedTopic() {
        return new NewTopic("payment.completed", 1, (short) 1);
    }

    @Bean
    public NewTopic orderCreatedTopic() {
        return new NewTopic("order.created", 1, (short) 1);
    }

    @Bean
    public ConsumerFactory<String, PaymentCompletedEvent> paymentCompletedEventConsumerFactory(
            KafkaProperties properties
    ) {
        return new DefaultKafkaConsumerFactory<>(
                properties.buildConsumerProperties(),
                new StringDeserializer(),
                new JacksonJsonDeserializer<>(PaymentCompletedEvent.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> paymentCompletedEventConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, PaymentCompletedEvent> paymentCompletedEventConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentCompletedEventConsumerFactory);
        return factory;
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
            ConsumerFactory<String, OrderCreatedEvent> orderConfirmedEventConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConfirmedEventConsumerFactory);
        return factory;
    }
}
