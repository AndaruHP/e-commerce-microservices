package com.ecommerce.shippingservice.event;

import java.util.UUID;

public record OrderConfirmedEvent(
        UUID orderId,
        UUID userId,
        String shippingAddress
) {
}
