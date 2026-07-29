package com.ecommerce.authservice.dto;

public record RegisterRequest(
        String name,
        String email,
        String password
) {
}
