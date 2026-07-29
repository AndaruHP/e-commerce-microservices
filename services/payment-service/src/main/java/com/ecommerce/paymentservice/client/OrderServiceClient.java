package com.ecommerce.paymentservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class OrderServiceClient {
    private final RestClient restClient;

    @Value("${services.order-service.url}")
    private String baseUrl;

    public Map<String, Object> getOrder(UUID orderId) {
        return restClient.get()
                .uri(baseUrl + "/api/orders/{id}", orderId)
                .retrieve()
                .body(Map.class);
    }

    public void updateOrderStatus(UUID orderId, String status) {
        restClient.patch()
                .uri(baseUrl + "/api/orders/{orderId}/status", orderId)
                .body(Map.of("status", status))
                .retrieve()
                .toBodilessEntity();
    }
}
