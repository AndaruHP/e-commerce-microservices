package com.ecommerce.inventoryservice.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private UUID productId;
    private Integer quantity;
    private LocalDateTime updatedAt;
}
