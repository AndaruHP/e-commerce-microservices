package com.ecommerce.orderservice.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private UUID id;
    private UUID orderId;
    private UUID productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}
