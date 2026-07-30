package com.ecommerce.paymentservice.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void paymentCompleted(PaymentCompletedEvent event) {
        kafkaTemplate.send("payment.completed", event.orderId().toString(), event);
    }
}
