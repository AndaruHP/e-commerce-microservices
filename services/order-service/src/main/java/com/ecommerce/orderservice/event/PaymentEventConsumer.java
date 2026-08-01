package com.ecommerce.orderservice.event;

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
        orderService.updateStatus(event.orderId(), new UpdateStatusRequest("CONFIRMED"));
        orderEventPublisher.orderConfirmed(new OrderConfirmedEvent(
                event.orderId(),
                event.userId()
        ));
    }
}
