package com.ecommerce.authservice.repository;

import com.ecommerce.authservice.entity.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken save(RefreshToken refreshToken);
    void deleteByUserId(UUID userId);
}
