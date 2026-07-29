package com.ecommerce.shippingservice.dto;

public record UpdateTrackingRequest(
    String carrier,
    String trackingNumber
) {}
