package com.ecommerce.authservice.repository;

import com.ecommerce.authservice.entity.RefreshToken;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<RefreshToken> refreshTokenRowMapper = (rs, rowNum) -> RefreshToken.builder()
            .id(rs.getObject("id", UUID.class))
            .userId(rs.getObject("user_id", UUID.class))
            .token(rs.getString("token"))
            .revoked(rs.getBoolean("revoked"))
            .expiresAt(rs.getTimestamp("expires_at").toLocalDateTime())
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .build();

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        String sql = "SELECT * FROM refresh_tokens WHERE token = ?";
        return jdbcTemplate.query(sql, refreshTokenRowMapper, token)
                .stream()
                .findFirst();
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        String sql = "INSERT INTO refresh_tokens (id, user_id, token, revoked, expires_at, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "revoked = EXCLUDED.revoked";

        jdbcTemplate.update(sql,
                refreshToken.getId(),
                refreshToken.getUserId(),
                refreshToken.getToken(),
                refreshToken.isRevoked(),
                refreshToken.getExpiresAt(),
                refreshToken.getCreatedAt()
        );
        return refreshToken;
    }

    @Override
    public void deleteByUserId(UUID userId) {
        String sql = "DELETE FROM refresh_tokens WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
