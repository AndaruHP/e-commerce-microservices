package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.dto.StockRequest;
import com.ecommerce.inventoryservice.dto.StockResponse;

import java.util.List;
import java.util.UUID;

public interface InventoryService {
    List<StockResponse> getAllStock();
    StockResponse getStock(UUID productId);
    StockResponse createStock(StockRequest request);
    StockResponse updateStock(UUID productId, StockRequest request);
    StockResponse addStock(UUID productId, StockRequest request);
    StockResponse deductStock(UUID productId, StockRequest request);
    void deleteStock(UUID productId);
}
