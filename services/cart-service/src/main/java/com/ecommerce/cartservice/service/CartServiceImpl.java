package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.client.ProductServiceClient;
import com.ecommerce.cartservice.dto.AddToCartRequest;
import com.ecommerce.cartservice.dto.CartItemResponse;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.UpdateCartItemRequest;
import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.entity.CartItem;
import com.ecommerce.cartservice.repository.CartItemRepository;
import com.ecommerce.cartservice.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;

    @Override
    public CartResponse getCart(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(null);

        if (cart == null) {
            return new CartResponse(null, userId, List.of(), BigDecimal.ZERO, 0);
        }

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(UUID userId, AddToCartRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .id(UUID.randomUUID())
                            .userId(userId)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return cartRepository.save(newCart);
                });

        List<CartItem> existingItems = cartItemRepository.findByCartId(cart.getId());
        CartItem existingItem = existingItems.stream()
                .filter(item -> item.getProductId().equals(request.productId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
            cartItemRepository.save(existingItem);
        } else {
            Map<String, Object> product = productServiceClient.getProduct(request.productId());
            String productName = (String) product.get("name");
            BigDecimal price = new BigDecimal(product.get("price").toString());

            CartItem newItem = CartItem.builder()
                    .id(UUID.randomUUID())
                    .cartId(cart.getId())
                    .productId(request.productId())
                    .productName(productName)
                    .price(price)
                    .quantity(request.quantity())
                    .createdAt(LocalDateTime.now())
                    .build();
            cartItemRepository.save(newItem);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateItem(UUID userId, UUID itemId, UpdateCartItemRequest request) {
        Cart cart = getCartOrThrow(userId);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (request.quantity() <= 0) {
            cartItemRepository.deleteById(itemId);
        } else {
            item.setQuantity(request.quantity());
            cartItemRepository.save(item);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public void removeItem(UUID userId, UUID itemId) {
        Cart cart = getCartOrThrow(userId);
        cartItemRepository.deleteById(itemId);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(UUID userId) {
        Cart cart = getCartOrThrow(userId);
        cartItemRepository.deleteByCartId(cart.getId());
        cartRepository.deleteById(cart.getId());
    }

    private Cart getCartOrThrow(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    private CartResponse buildCartResponse(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());

        List<CartItemResponse> itemResponses = items.stream()
                .map(item -> new CartItemResponse(
                        item.getId(),
                        item.getProductId(),
                        item.getProductName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();

        BigDecimal totalPrice = itemResponses.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = itemResponses.stream()
                .mapToInt(CartItemResponse::quantity)
                .sum();

        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                itemResponses,
                totalPrice,
                totalItems
        );
    }
}
