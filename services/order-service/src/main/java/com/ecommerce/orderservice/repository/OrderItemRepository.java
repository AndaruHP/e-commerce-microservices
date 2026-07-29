package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository {
    List<OrderItem> findByOrderId(UUID orderId);
    OrderItem save(OrderItem item);
    List<OrderItem> saveAll(List<OrderItem> items);
}
