package com.ecommerce.notificationservice.event;

import java.util.UUID;

public record OrderConfirmedEvent(
        UUID orderId,
        UUID userId,
        String shippingAddress
) {
}
