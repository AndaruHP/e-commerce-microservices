package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.dto.StockRequest;
import com.ecommerce.inventoryservice.dto.StockResponse;
import com.ecommerce.inventoryservice.entity.Stock;
import com.ecommerce.inventoryservice.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final StockRepository stockRepository;

    @Override
    public List<StockResponse> getAllStock() {
        return stockRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public StockResponse getStock(UUID productId) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        return toResponse(stock);
    }

    @Override
    @Transactional
    public StockResponse createStock(StockRequest request) {
        if (stockRepository.findByProductId(request.productId()).isPresent()) {
            throw new RuntimeException("Stock already exists for this product");
        }

        Stock stock = Stock.builder()
                .productId(request.productId())
                .quantity(request.quantity())
                .updatedAt(LocalDateTime.now())
                .build();

        return toResponse(stockRepository.save(stock));
    }

    @Override
    @Transactional
    public StockResponse updateStock(UUID productId, StockRequest request) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        stock.setQuantity(request.quantity());
        stock.setUpdatedAt(LocalDateTime.now());
        return toResponse(stockRepository.save(stock));
    }

    @Override
    @Transactional
    public StockResponse addStock(UUID productId, StockRequest request) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        stock.setQuantity(stock.getQuantity() + request.quantity());
        stock.setUpdatedAt(LocalDateTime.now());
        return toResponse(stockRepository.save(stock));
    }

    @Override
    @Transactional
    public StockResponse deductStock(UUID productId, StockRequest request) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        int newQuantity = stock.getQuantity() - request.quantity();
        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient stock");
        }

        stock.setQuantity(newQuantity);
        stock.setUpdatedAt(LocalDateTime.now());
        return toResponse(stockRepository.save(stock));
    }

    @Override
    @Transactional
    public void deleteStock(UUID productId) {
        stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        stockRepository.deleteByProductId(productId);
    }

    private StockResponse toResponse(Stock stock) {
        return new StockResponse(
                stock.getProductId(),
                stock.getQuantity(),
                stock.getUpdatedAt()
        );
    }
}
