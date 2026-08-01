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

    public void orderConfirmed(OrderConfirmedEvent event) {
        kafkaTemplate.send("order.confirmed", event.orderId().toString(), event);
    }

    public void orderCancelled(OrderCancelledEvent event) {
        kafkaTemplate.send("order.cancelled", event.orderId().toString(), event);
    }
}
