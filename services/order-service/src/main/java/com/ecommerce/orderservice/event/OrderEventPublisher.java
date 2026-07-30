package com.ecommerce.orderservice.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void orderCreated(OrderCreatedEvent event) {
        kafkaTemplate.send("order.created", event.orderId().toString(), event);
    }
}
