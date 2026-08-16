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
                        .settings(s -> s
                                .analysis(a -> a
                                        .analyzer("autocomplete", an -> an
                                                .custom(cu -> cu
                                                        .tokenizer("standard")
                                                        .filter("lowercase", "edge_gram")))
                                        .filter("edge_gram", tf -> tf
                                                .definition(d -> d
                                                        .edgeNgram(eg -> eg.minGram(2).maxGram(20))))))
                        .mappings(m -> m
                                .properties("name", p -> p.text(t -> t
                                        .fields("keyword", f -> f.keyword(k -> k))
                                        .fields("autocomplete", f -> f.text(a -> a
                                                .analyzer("autocomplete")
                                                .searchAnalyzer("standard")))))
                                .properties("description", p -> p.text(t -> t))
                                .properties("price", p -> p.double_(d -> d))
                                .properties("stock", p -> p.integer(i -> i))
                                .properties("createdAt", p -> p.date(d -> d))
                                .properties("updatedAt", p -> p.date(d -> d))
                        )
                );
            }
        };
    }
}
