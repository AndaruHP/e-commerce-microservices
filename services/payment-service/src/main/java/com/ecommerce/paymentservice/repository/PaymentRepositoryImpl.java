package com.ecommerce.paymentservice.repository;

import com.ecommerce.paymentservice.entity.Payment;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Payment> paymentRowMapper = (rs, rowNum) -> Payment.builder()
        .id(rs.getObject("id", UUID.class))
        .orderId(rs.getObject("order_id", UUID.class))
        .userId(rs.getObject("user_id", UUID.class))
        .amount(rs.getBigDecimal("amount"))
        .status(rs.getString("status"))
        .paymentMethod(rs.getString("payment_method"))
        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
        .build();

    @Override
    public Optional<Payment> findById(UUID id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        return jdbcTemplate.query(sql, paymentRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        return jdbcTemplate.query(sql, paymentRowMapper, orderId)
                .stream()
                .findFirst();
    }

    @Override
    public Payment save(Payment payment) {
        String sql = "INSERT INTO payments (id, order_id, user_id, amount, status, payment_method, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON CONFLICT (id) DO UPDATE SET " +
                     "status = EXCLUDED.status, updated_at = EXCLUDED.updated_at";
        jdbcTemplate.update(sql,
            payment.getId(),
            payment.getOrderId(),
            payment.getUserId(),
            payment.getAmount(),
            payment.getStatus(),
            payment.getPaymentMethod(),
            payment.getCreatedAt(),
            payment.getUpdatedAt()
        );
        return payment;
    }
}
