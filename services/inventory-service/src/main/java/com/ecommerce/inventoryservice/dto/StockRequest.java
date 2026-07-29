package com.ecommerce.inventoryservice.dto;

import java.util.UUID;

public record StockRequest(
        UUID productId,
        Integer quantity
) {
}
