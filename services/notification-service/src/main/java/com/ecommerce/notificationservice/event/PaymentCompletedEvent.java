package com.ecommerce.notificationservice.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PaymentCompletedEvent(
        UUID paymentId,
        UUID orderId,
        UUID userId,
        BigDecimal amount,
        LocalDateTime completedAt
) {
}
