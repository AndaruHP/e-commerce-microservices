package com.ecommerce.inventoryservice.event;

import java.util.List;
import java.util.UUID;

public record OrderConfirmedEvent(
        UUID orderId,
        UUID userId,
        List<OrderItemEvent> items
) {
    public record OrderItemEvent(
            UUID productId,
            Integer quantity
    ) {}
}
