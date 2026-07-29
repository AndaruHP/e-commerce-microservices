package com.ecommerce.orderservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryServiceClient {
    private final RestClient restClient;

    @Value("${services.inventory-service.url}")
    private String baseUrl;

    public void deductStock(UUID productId, int quantity) {
        restClient.patch()
                .uri(baseUrl + "/api/inventory/{productId}/deduct", productId)
                .body(Map.of("quantity", quantity))
                .retrieve()
                .toBodilessEntity();
    }
}
