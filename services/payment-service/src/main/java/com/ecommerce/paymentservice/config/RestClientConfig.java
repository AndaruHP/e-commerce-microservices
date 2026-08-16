package com.ecommerce.paymentservice.config;

import com.ecommerce.securitycommon.ServiceTokenProvider;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient(ObservationRegistry observationRegistry, ServiceTokenProvider serviceTokenProvider) {
        return RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    request.getHeaders().setBearerAuth(serviceTokenProvider.getToken());
                    return execution.execute(request, body);
                })
                .observationRegistry(observationRegistry)
                .build();
    }
}
