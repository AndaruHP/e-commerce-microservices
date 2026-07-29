package com.ecommerce.shippingservice.dto;

import java.util.UUID;

public record CreateShippingRequest(
    UUID orderId,
    UUID userId,
    String address
) {}
