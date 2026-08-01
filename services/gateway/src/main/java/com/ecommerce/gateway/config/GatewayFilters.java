package com.ecommerce.gateway.config;

import io.netty.channel.ChannelOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class GatewayFilters {
    @Bean
    public GlobalFilter correlationIdAndLoggingFilter() {
        return new CorrelationIDAndLoggingFilter();
    }

    @Bean
    public HttpClientCustomizer httpClientCustomizer() {
        return httpClient -> httpClient
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofSeconds(10));
    }

    static class CorrelationIDAndLoggingFilter implements GlobalFilter, Ordered {
        private static final Logger log = LoggerFactory.getLogger(CorrelationIDAndLoggingFilter.class);
        private static final String X_REQUEST_ID = "X-Request-Id" ;

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            long start = System.currentTimeMillis();

            String incomingId = exchange.getRequest().getHeaders().getFirst(X_REQUEST_ID);
            String requestId = (incomingId == null || incomingId.isBlank())
                    ? UUID.randomUUID().toString()
                    : incomingId;

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header(X_REQUEST_ID, requestId)
                    .build();

            exchange.getResponse().getHeaders().set(X_REQUEST_ID, requestId);

            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

            return chain.filter(mutatedExchange).doOnSuccess(ignored -> {
                long duration = System.currentTimeMillis() - start;
                log.info("[{}] {} {} -> {} ({}ms}",
                        requestId,
                        mutatedExchange.getRequest().getMethod(),
                        mutatedExchange.getRequest().getPath(),
                        mutatedExchange.getResponse().getStatusCode(),
                        duration);
            });
        }

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }
    }
}
