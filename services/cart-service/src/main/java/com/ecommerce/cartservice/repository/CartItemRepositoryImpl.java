package com.ecommerce.cartservice.repository;

import com.ecommerce.cartservice.entity.CartItem;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<CartItem> cartItemRowMapper = (rs, rowNum) -> CartItem.builder()
            .id(rs.getObject("id", UUID.class))
            .cartId(rs.getObject("cart_id", UUID.class))
            .productId(rs.getObject("product_id", UUID.class))
            .productName(rs.getString("product_name"))
            .price(rs.getBigDecimal("price"))
            .quantity(rs.getInt("quantity"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .build();

    @Override
    public List<CartItem> findByCartId(UUID cartId) {
        String sql = "SELECT * FROM cart_items WHERE cart_id = ?";
        return jdbcTemplate.query(sql, cartItemRowMapper, cartId);
    }

    @Override
    public Optional<CartItem> findById(UUID id) {
        String sql = "SELECT * FROM cart_items WHERE id = ?";
        return jdbcTemplate.query(sql, cartItemRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public CartItem save(CartItem item) {
        String sql = "INSERT INTO cart_items (id, cart_id, product_id, product_name, price, quantity, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "quantity = EXCLUDED.quantity, product_name = EXCLUDED.product_name, price = EXCLUDED.price";
        jdbcTemplate.update(sql,
                item.getId(),
                item.getCartId(),
                item.getProductId(),
                item.getProductName(),
                item.getPrice(),
                item.getQuantity(),
                item.getCreatedAt()
        );
        return item;
    }

    @Override
    public void deleteById(UUID id) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteByCartId(UUID cartId) {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        jdbcTemplate.update(sql, cartId);
    }
}
