package com.ecommerce.inventoryservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockResponse(
        UUID productId,
        Integer quantity,
        LocalDateTime updatedAt
) {
}
