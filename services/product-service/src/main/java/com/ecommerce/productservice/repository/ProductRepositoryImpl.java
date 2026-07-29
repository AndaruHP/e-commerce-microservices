package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> Product.builder()
            .id(rs.getObject("id", UUID.class))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .price(rs.getBigDecimal("price"))
            .stock(rs.getInt("stock"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .build();

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM products ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.query(sql, productRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Product save(Product product) {
        String sql = "INSERT INTO products (id, name, description, price, stock, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "name = EXCLUDED.name, description = EXCLUDED.description, " +
                "price = EXCLUDED.price, stock = EXCLUDED.stock, " +
                "updated_at = EXCLUDED.updated_at";
        jdbcTemplate.update(sql,
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
        return product;
    }

    @Override
    public void deleteById(UUID id) {
        String sql = "DELETE FROM products WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
