package com.ecommerce.cartservice.repository;

import com.ecommerce.cartservice.entity.CartItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository {
    List<CartItem> findByCartId(UUID cartId);
    Optional<CartItem> findById(UUID id);
    CartItem save(CartItem item);
    void deleteById(UUID id);
    void deleteByCartId(UUID cartId);

}
