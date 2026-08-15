package com.ecommerce.productservice.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    @Bean
    ApplicationRunner createProductsIndex(ElasticsearchClient client) {
        return args -> {
            boolean exists = client.indices().exists(e -> e.index("products")).value();
            if (!exists) {
                client.indices().create(c -> c
                        .index("products")
                        .mappings(m -> m
                                .properties("name", p -> p.text(t -> t
                                        .fields("keyword", f -> f.keyword(k -> k))))
                                .properties("description", p -> p.text(t -> t))
                                .properties("price", p -> p.double_(d -> d))
                                .properties("stock", p -> p.integer(i -> i))
                                .properties("createdAt", p -> p.date(d -> d))
                                .properties("updatedAt", p -> p.date(d -> d))
                        ));
            }
        };
    }
}
