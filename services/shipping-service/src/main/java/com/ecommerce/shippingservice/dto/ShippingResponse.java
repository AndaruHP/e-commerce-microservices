package com.ecommerce.shippingservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShippingResponse(
    UUID id,
    UUID orderId,
    UUID userId,
    String address,
    String status,
    String carrier,
    String trackingNumber,
    LocalDateTime shippedAt,
    LocalDateTime deliveredAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
