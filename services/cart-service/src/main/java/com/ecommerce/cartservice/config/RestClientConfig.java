package com.ecommerce.cartservice.config;

import com.ecommerce.securitycommon.ServiceTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient(ServiceTokenProvider serviceTokenProvider) {
        return RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    request.getHeaders().setBearerAuth(serviceTokenProvider.getToken());
                    return execution.execute(request, body);
                })
                .build();
    }
}
