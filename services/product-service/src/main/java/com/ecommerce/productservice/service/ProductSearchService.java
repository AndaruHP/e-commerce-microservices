package com.ecommerce.productservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ecommerce.productservice.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private static final String INDEX = "products";
    private final ElasticsearchClient  elasticsearchClient;

    public void index(ProductResponse product) {
        try {
            elasticsearchClient.index(i -> i
                    .index(INDEX)
                    .id(product.id().toString())
                    .document(product)
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed index the product into Elasticsearch", e);
        }
    }

    public void remove(UUID id) {
        try {
            elasticsearchClient.delete(d -> d.index(INDEX).id(id.toString()));
        } catch (IOException e) {
            throw new RuntimeException("Failed deleting the product from Elasticsearch");
        }
    }

    public List<ProductResponse> search(String q, Double minPrice, Double maxPrice) {
        List<Query> must = new ArrayList<>();

        if (q != null && !q.isBlank()) {
            must.add(Query.of(x -> x.multiMatch(m -> m
                    .fields("name.autocomplete^3", "name^2", "description")
                    .query(q)))
            );
        }

        if (minPrice != null || maxPrice != null) {
            must.add(Query.of(x -> x.range(r -> r.number(n -> {
                var b = n.field("price");
                if (minPrice != null) b = b.gte(minPrice);
                if (maxPrice != null) b = b.lte(maxPrice);
                return b;
            }))));
        }

        Query query = must.isEmpty()
                ? Query.of(x -> x.matchAll(m -> m))
                : Query.of(x -> x.bool(b -> b.must(must)));

        try {
            SearchResponse<ProductResponse> response = elasticsearchClient.search(s -> s
                            .index(INDEX)
                            .query(query),
                    ProductResponse.class);
            return response.hits().hits().stream().map(Hit::source).toList();
        } catch (IOException e) {
            throw new RuntimeException("Gagal mencari produk", e);
        }
    }
}
