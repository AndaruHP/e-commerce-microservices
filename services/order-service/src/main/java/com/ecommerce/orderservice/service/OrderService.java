package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.dto.UpdateStatusRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse getOrderById(UUID id);
    List<OrderResponse> getOrdersByUserId(UUID userId);
    List<OrderResponse> getAllOrders();
    OrderResponse updateStatus(UUID id, UpdateStatusRequest request);
}
