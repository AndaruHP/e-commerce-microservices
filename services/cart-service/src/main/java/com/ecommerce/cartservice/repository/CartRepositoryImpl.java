package com.ecommerce.cartservice.repository;

import com.ecommerce.cartservice.entity.Cart;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class CartRepositoryImpl implements CartRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Cart> cartRowMapper = (rs, rowNum) -> Cart.builder()
            .id(rs.getObject("id", UUID.class))
            .userId(rs.getObject("user_id", UUID.class))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .build();

    @Override
    public Optional<Cart> findByUserId(UUID userId) {
        String sql = "SELECT * FROM carts WHERE user_id = ?";
        return jdbcTemplate.query(sql, cartRowMapper, userId)
                .stream()
                .findFirst();
    }

    @Override
    public Cart save(Cart cart) {
        String sql = "INSERT INTO carts (id, user_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET updated_at = EXCLUDED.updated_at";
        jdbcTemplate.update(sql,
                cart.getId(),
                cart.getUserId(),
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
        return cart;
    }

    @Override
    public void deleteById(UUID id) {
        String sql = "DELETE FROM carts WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
