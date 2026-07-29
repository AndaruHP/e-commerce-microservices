package com.ecommerce.inventoryservice.repository;

import com.ecommerce.inventoryservice.entity.Stock;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class StockRepositoryImpl implements StockRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Stock> stockRowMapper = (rs, rowNum) -> Stock.builder()
            .productId(rs.getObject("product_id", UUID.class))
            .quantity(rs.getInt("quantity"))
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .build();

    @Override
    public List<Stock> findAll() {
        String sql = "SELECT * FROM stocks";
        return jdbcTemplate.query(sql, stockRowMapper);
    }

    @Override
    public Optional<Stock> findByProductId(UUID productId) {
        String sql = "SELECT * FROM stocks WHERE product_id = ?";
        return jdbcTemplate.query(sql, stockRowMapper, productId)
                .stream()
                .findFirst();
    }

    @Override
    public Stock save(Stock stock) {
        String sql = "INSERT INTO stocks (product_id, quantity, updated_at) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT (product_id) DO UPDATE SET " +
                "quantity = EXCLUDED.quantity, updated_at = EXCLUDED.updated_at";
        jdbcTemplate.update(sql,
                stock.getProductId(),
                stock.getQuantity(),
                stock.getUpdatedAt()
        );
        return stock;
    }

    @Override
    public void deleteByProductId(UUID productId) {
        String sql = "DELETE FROM stocks WHERE product_id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
