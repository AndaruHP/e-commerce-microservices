package com.ecommerce.paymentservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
    UUID id,
    UUID orderId,
    UUID userId,
    BigDecimal amount,
    String status,
    String paymentMethod,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
