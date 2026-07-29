package com.ecommerce.cartservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID cartId,
        UUID userId,
        List<CartItemResponse> items,
        BigDecimal totalPrice,
        Integer totalItems
) {
}
