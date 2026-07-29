package com.ecommerce.authservice.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    private UUID id;
    private UUID userId;
    private String token;
    private boolean revoked;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
