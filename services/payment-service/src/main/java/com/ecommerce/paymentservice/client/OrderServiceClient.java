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
                .uri(baseUrl + "/api/order/{id}", orderId)
                .retrieve()
                .body(Map.class);
    }
}
