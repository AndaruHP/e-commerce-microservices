package com.ecommerce.cartservice.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private UUID id;
    private UUID cartId;
    private UUID productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private LocalDateTime createdAt;
}
