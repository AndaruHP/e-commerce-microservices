package com.ecommerce.cartservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AddToCartRequest(
        UUID productId,
        String productName,
        BigDecimal price,
        Integer quantity
) {
}
