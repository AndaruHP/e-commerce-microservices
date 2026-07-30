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

    @KafkaListener(topics = "order.confirmed", groupId = "inventory-service")
    public void handleOrderConfirmed(OrderConfirmedEvent event) {
        for (var item: event.items()) {
            inventoryService.deductStock(
                    item.productId(),
                    new StockRequest(item.productId(), item.quantity())
            );
        }
    }
}
