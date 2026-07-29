package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> Order.builder()
            .id(rs.getObject("id", UUID.class))
            .userId(rs.getObject("user_id", UUID.class))
            .status(rs.getString("status"))
            .totalPrice(rs.getBigDecimal("total_price"))
            .shippingAddress(rs.getString("shipping_address"))
            .notes(rs.getString("notes"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .build();

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, orderRowMapper);
    }

    @Override
    public List<Order> findByUserId(UUID userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, orderRowMapper, userId);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Order save(Order order) {
        String sql = "INSERT INTO orders (id, user_id, status, total_price, shipping_address, notes, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "status = EXCLUDED.status, updated_at = EXCLUDED.updated_at";
        jdbcTemplate.update(sql,
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getShippingAddress(),
                order.getNotes(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
        return order;
    }
}
