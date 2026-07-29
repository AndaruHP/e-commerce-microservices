package com.ecommerce.notificationservice.entity;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private UUID id;
    private UUID userId;
    private String title;
    private String message;
    private String type;
    private String status;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}
