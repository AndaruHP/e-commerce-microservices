package com.ecommerce.notificationservice.event;

import com.ecommerce.notificationservice.dto.SendNotificationRequest;
import com.ecommerce.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "payment.completed",
            groupId = "notification-service",
            containerFactory = "paymentCompletedEventConcurrentKafkaListenerContainerFactory"
    )
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        notificationService.sendNotification(new SendNotificationRequest(
                event.userId(),
                "Payment Completed",
                "Your payment of $" + event.amount() + " for order " + event.orderId() + " has been completed.",
                "EMAIL"
        ));
    }

    @KafkaListener(
            topics = "order.created",
            groupId = "notification-service",
            containerFactory = "orderCreatedEventConcurrentKafkaListenerContainerFactory"
    )
    public void handleOrderCreated(OrderCreatedEvent event) {
        notificationService.sendNotification(new SendNotificationRequest(
                event.userId(),
                "Order Created",
                "Your order " + event.orderId() + " has been created.",
                "EMAIL"
        ));
    }

    @KafkaListener(
            topics = "order.confirmed",
            groupId = "notification-service",
            containerFactory = "orderConfirmedEventConcurrentKafkaListenerContainerFactory"
    )
    public void handleOrderConfirmed(OrderConfirmedEvent event) {
        notificationService.sendNotification(new SendNotificationRequest(
                event.userId(),
                "Order Confirmed",
                "Your order " + event.orderId() + " has been confirmed and is being processed.",
                "EMAIL"
        ));
    }
}
