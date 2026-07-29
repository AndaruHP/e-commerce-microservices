package com.ecommerce.notificationservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
    UUID id,
    UUID userId,
    String title,
    String message,
    String type,
    String status,
    LocalDateTime sentAt,
    LocalDateTime createdAt
) {}
