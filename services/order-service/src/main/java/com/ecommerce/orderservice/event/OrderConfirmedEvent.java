package com.ecommerce.orderservice.event;

import java.util.UUID;

public record OrderConfirmedEvent(
        UUID orderId,
        UUID userId
) {
}
