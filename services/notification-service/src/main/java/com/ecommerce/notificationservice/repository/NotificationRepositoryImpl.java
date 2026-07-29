package com.ecommerce.notificationservice.repository;

import com.ecommerce.notificationservice.entity.Notification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    public NotificationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Notification> notificationRowMapper = (rs, rowNum) -> Notification.builder()
        .id(rs.getObject("id", UUID.class))
        .userId(rs.getObject("user_id", UUID.class))
        .title(rs.getString("title"))
        .message(rs.getString("message"))
        .type(rs.getString("type"))
        .status(rs.getString("status"))
        .sentAt(rs.getTimestamp("sent_at") != null ? rs.getTimestamp("sent_at").toLocalDateTime() : null)
        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
        .build();

    @Override
    public Optional<Notification> findById(UUID id) {
        String sql = "SELECT * FROM notifications WHERE id = ?";
        return jdbcTemplate.query(sql, notificationRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Notification> findByUserId(UUID userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, notificationRowMapper, userId);
    }

    @Override
    public List<Notification> findByUserIdAndStatus(UUID userId, String status) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND status = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, notificationRowMapper, userId, status);
    }

    @Override
    public Notification save(Notification notification) {
        String sql = "INSERT INTO notifications (id, user_id, title, message, type, status, sent_at, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON CONFLICT (id) DO UPDATE SET " +
                     "status = EXCLUDED.status";
        jdbcTemplate.update(sql,
            notification.getId(),
            notification.getUserId(),
            notification.getTitle(),
            notification.getMessage(),
            notification.getType(),
            notification.getStatus(),
            notification.getSentAt(),
            notification.getCreatedAt()
        );
        return notification;
    }
}
