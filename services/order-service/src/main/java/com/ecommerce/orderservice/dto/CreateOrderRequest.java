package com.ecommerce.orderservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        UUID userId,
        String shippingAddress,
        String notes
) {
}
