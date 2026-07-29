package com.ecommerce.authservice.dto;

public record RegisterResponse(
        String id,
        String name,
        String email,
        String role
) {
}
