package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    List<Order> findAll();
    List<Order> findByUserId(UUID userId);
    Optional<Order> findById(UUID id);
    Order save(Order order);
}
