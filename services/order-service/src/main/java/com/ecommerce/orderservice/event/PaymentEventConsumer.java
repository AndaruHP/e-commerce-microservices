package com.ecommerce.orderservice.event;

import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.dto.UpdateStatusRequest;
import com.ecommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {
    private final OrderService orderService;
    private final OrderEventPublisher orderEventPublisher;

    @KafkaListener(topics = "payment.completed", groupId = "order-service")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        OrderResponse order = orderService.updateStatus(event.orderId(), new UpdateStatusRequest("CONFIRMED"));
        orderEventPublisher.orderConfirmed(new OrderConfirmedEvent(
                order.id(),
                order.userId(),
                order.shippingAddress()
        ));
    }
}
