package com.ecommerce.shippingservice.event;

import com.ecommerce.shippingservice.dto.CreateShippingRequest;
import com.ecommerce.shippingservice.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConfirmedEventConsumer {
    private final ShippingService shippingService;

    @KafkaListener(
            topics = "order.confirmed",
            groupId = "shipping-service",
            containerFactory = "orderConfirmedEventConcurrentKafkaListenerContainerFactory"
    )
    public void handleOrderConfirmed(OrderConfirmedEvent event) {
        shippingService.createShipping(new CreateShippingRequest(
                event.orderId(), event.userId(), event.shippingAddress()
        ));
    }
}
