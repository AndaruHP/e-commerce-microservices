package com.ecommerce.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter

public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdAt;
}
