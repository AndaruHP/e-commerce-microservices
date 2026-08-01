package com.ecommerce.inventoryservice.event;

import com.ecommerce.inventoryservice.dto.StockRequest;
import com.ecommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "order.created",
            groupId = "inventory-service",
            containerFactory = "orderCreatedEventConcurrentKafkaListenerContainerFactory"
    )
    public void handleOrderCreated(OrderCreatedEvent event) {
        for (var item: event.items()) {
            inventoryService.deductStock(
                    item.productId(),
                    new StockRequest(item.productId(), item.quantity())
            );
        }
    }

    @KafkaListener(
            topics = "order.cancelled",
            groupId = "inventory-service",
            containerFactory = "orderCancelledEventConcurrentKafkaListenerContainerFactory"
    )
    public void handleOrderCancelled(OrderCancelledEvent event) {
        for (var item: event.items()) {
            inventoryService.addStock(
                    item.productId(),
                    new StockRequest(item.productId(), item.quantity())
            );
        }
    }
}
