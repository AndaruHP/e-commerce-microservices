package com.ecommerce.inventoryservice.repository;

import com.ecommerce.inventoryservice.entity.Stock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository {
    List<Stock> findAll();
    Optional<Stock> findByProductId(UUID productId);
    Stock save(Stock stock);
    void deleteByProductId(UUID productId);
}
