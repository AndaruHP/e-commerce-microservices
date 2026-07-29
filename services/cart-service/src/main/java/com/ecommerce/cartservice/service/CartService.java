package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.dto.AddToCartRequest;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.UpdateCartItemRequest;

import java.util.UUID;

public interface CartService {
    CartResponse getCart(UUID userId);
    CartResponse addItem(UUID userId, AddToCartRequest request);
    CartResponse updateItem(UUID userId, UUID itemId, UpdateCartItemRequest request);
    void removeItem(UUID userId, UUID itemId);
    void clearCart(UUID userId);
}
