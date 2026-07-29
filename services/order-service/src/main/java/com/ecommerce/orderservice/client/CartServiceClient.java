package com.ecommerce.orderservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CartServiceClient {
    private final RestClient restClient;

    @Value("${services.cart-service.url}")
    private String baseUrl;

    @SuppressWarnings("unchecked")
    public Map<String, Object> getCart(UUID userId) {
        return restClient.get()
                .uri(baseUrl + "/api/cart/{userId}", userId)
                .retrieve()
                .body(Map.class);
    }

    public void clearCart(UUID userId) {
        restClient.delete()
                .uri(baseUrl + "/api/cart/{userId}", userId)
                .retrieve()
                .toBodilessEntity();
    }
}
