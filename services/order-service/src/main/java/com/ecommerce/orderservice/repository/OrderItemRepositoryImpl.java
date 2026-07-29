package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<OrderItem> orderItemRowMapper = (rs, rowNum) -> OrderItem.builder()
            .id(rs.getObject("id", UUID.class))
            .orderId(rs.getObject("order_id", UUID.class))
            .productId(rs.getObject("product_id", UUID.class))
            .productName(rs.getString("product_name"))
            .price(rs.getBigDecimal("price"))
            .quantity(rs.getInt("quantity"))
            .build();

    @Override
    public List<OrderItem> findByOrderId(UUID orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
    }

    @Override
    public OrderItem save(OrderItem item) {
        String sql = "INSERT INTO order_items (id, order_id, product_id, product_name, price, quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                item.getId(),
                item.getOrderId(),
                item.getProductId(),
                item.getProductName(),
                item.getPrice(),
                item.getQuantity()
        );
        return item;
    }

    @Override
    public List<OrderItem> saveAll(List<OrderItem> items) {
        String sql = "INSERT INTO order_items (id, order_id, product_id, product_name, price, quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, items, items.size(), (ps, item) -> {
            ps.setObject(1, item.getId());
            ps.setObject(2, item.getOrderId());
            ps.setObject(3, item.getProductId());
            ps.setString(4, item.getProductName());
            ps.setBigDecimal(5, item.getPrice());
            ps.setInt(6, item.getQuantity());
        });
        return items;
    }
}
