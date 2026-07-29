package com.ecommerce.shippingservice.repository;

import com.ecommerce.shippingservice.entity.Shipment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ShippingRepositoryImpl implements ShippingRepository {

    private final JdbcTemplate jdbcTemplate;

    public ShippingRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Shipment> shipmentRowMapper = (rs, rowNum) -> Shipment.builder()
        .id(rs.getObject("id", UUID.class))
        .orderId(rs.getObject("order_id", UUID.class))
        .userId(rs.getObject("user_id", UUID.class))
        .address(rs.getString("address"))
        .status(rs.getString("status"))
        .carrier(rs.getString("carrier"))
        .trackingNumber(rs.getString("tracking_number"))
        .shippedAt(rs.getTimestamp("shipped_at") != null ? rs.getTimestamp("shipped_at").toLocalDateTime() : null)
        .deliveredAt(rs.getTimestamp("delivered_at") != null ? rs.getTimestamp("delivered_at").toLocalDateTime() : null)
        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
        .build();

    @Override
    public Optional<Shipment> findById(UUID id) {
        String sql = "SELECT * FROM shipments WHERE id = ?";
        return jdbcTemplate.query(sql, shipmentRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Shipment> findByOrderId(UUID orderId) {
        String sql = "SELECT * FROM shipments WHERE order_id = ?";
        return jdbcTemplate.query(sql, shipmentRowMapper, orderId)
                .stream()
                .findFirst();
    }

    @Override
    public List<Shipment> findByUserId(UUID userId) {
        String sql = "SELECT * FROM shipments WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, shipmentRowMapper, userId);
    }

    @Override
    public Shipment save(Shipment shipment) {
        String sql = "INSERT INTO shipments (id, order_id, user_id, address, status, carrier, tracking_number, " +
                     "shipped_at, delivered_at, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON CONFLICT (id) DO UPDATE SET " +
                     "status = EXCLUDED.status, carrier = EXCLUDED.carrier, " +
                     "tracking_number = EXCLUDED.tracking_number, " +
                     "shipped_at = EXCLUDED.shipped_at, delivered_at = EXCLUDED.delivered_at, " +
                     "updated_at = EXCLUDED.updated_at";
        jdbcTemplate.update(sql,
            shipment.getId(),
            shipment.getOrderId(),
            shipment.getUserId(),
            shipment.getAddress(),
            shipment.getStatus(),
            shipment.getCarrier(),
            shipment.getTrackingNumber(),
            shipment.getShippedAt() != null ? Timestamp.valueOf(shipment.getShippedAt()) : null,
            shipment.getDeliveredAt() != null ? Timestamp.valueOf(shipment.getDeliveredAt()) : null,
            shipment.getCreatedAt(),
            shipment.getUpdatedAt()
        );
        return shipment;
    }
}
