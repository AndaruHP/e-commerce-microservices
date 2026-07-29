package com.ecommerce.cartservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductServiceClient {

    private final RestClient restClient;

    @Value("${services.product-service.url}")
    private String baseUrl;

    public Map<String, Object> getProduct(UUID productId) {
        return restClient.get()
                .uri(baseUrl + "/api/product/{id}", productId)
                .retrieve()
                .body(Map.class);
    }
}