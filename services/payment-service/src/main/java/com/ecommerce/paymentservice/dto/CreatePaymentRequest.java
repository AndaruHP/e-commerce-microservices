package com.ecommerce.paymentservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentRequest(
    UUID orderId,
    UUID userId,
    String paymentMethod
) {}
