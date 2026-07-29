package com.ecommerce.cartservice.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private UUID id;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
