package com.ecommerce.authservice.dto;

public record LoginResponse(
        String id,
        String name,
        String email,
        String role,
        String accessToken,
        String refreshToken
) {
}
