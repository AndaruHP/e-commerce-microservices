package com.ecommerce.notificationservice.event;

import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        UUID userId,
        List<OrderItemEvent> items
) {
    public record OrderItemEvent(
            UUID productId,
            Integer quantity
    ) {}
}
