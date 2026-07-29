package com.ecommerce.cartservice.repository;

import com.ecommerce.cartservice.entity.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    Optional<Cart> findByUserId(UUID userId);
    Cart save(Cart cart);
    void deleteById(UUID id);
}
