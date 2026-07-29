package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.client.CartServiceClient;
import com.ecommerce.orderservice.client.InventoryServiceClient;
import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.OrderItemResponse;
import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.dto.UpdateStatusRequest;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.repository.OrderItemRepository;
import com.ecommerce.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartServiceClient cartServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Map<String, Object> cart = cartServiceClient.getCart(request.userId());
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) cart.get("items") ;

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal totalPrice = cartItems.stream()
                .map(item -> new BigDecimal(item.get("subtotal").toString()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .id(UUID.randomUUID())
                .userId(request.userId())
                .status(OrderStatus.PENDING.name())
                .totalPrice(totalPrice)
                .shippingAddress(request.shippingAddress())
                .notes(request.notes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> OrderItem.builder()
                        .id(UUID.randomUUID())
                        .orderId(order.getId())
                        .productId(UUID.fromString((String) item.get("productId")))
                        .productName((String) item.get("productName"))
                        .price(new BigDecimal(item.get("price").toString()))
                        .quantity((Integer) item.get("quantity"))
                        .build())
                .toList();

        orderItemRepository.saveAll(orderItems);

        cartServiceClient.clearCart(request.userId());

        for (OrderItem item: orderItems) {
            inventoryServiceClient.deductStock(item.getProductId(), item.getQuantity());
        }

        return buildOrderResponse(order);
    }

    @Override
    public OrderResponse getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return buildOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(UUID userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::buildOrderResponse)
                .toList();
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::buildOrderResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(UUID id, UpdateStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus newStatus = OrderStatus.valueOf(request.status().toUpperCase());

        OrderStatus currStatus = OrderStatus.valueOf(order.getStatus());
        validateStatusTransition(currStatus, newStatus);

        order.setStatus(newStatus.name());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        return buildOrderResponse(order);
    }

    private void validateStatusTransition(OrderStatus curr, OrderStatus next) {
        if (curr == OrderStatus.CANCELED) {
            throw new RuntimeException("Order already canceled");
        }

        if (next == OrderStatus.CANCELED) {
            return;
        }

        boolean valid = switch (curr) {
            case PENDING -> next == OrderStatus.CONFIRMED;
            case CONFIRMED -> next == OrderStatus.PROCESSING;
            case PROCESSING -> next == OrderStatus.SHIPPED;
            case SHIPPED -> next == OrderStatus.DELIVERED;
            default -> false;
        };

        if (!valid) {
            throw new RuntimeException("Cannot change status from " + curr + " to " + next);
        }
    }

    private OrderResponse buildOrderResponse(Order order) {
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());

        List<OrderItemResponse> itemResponses = items.stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getProductId(),
                        item.getProductName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getShippingAddress(),
                order.getNotes(),
                itemResponses,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
