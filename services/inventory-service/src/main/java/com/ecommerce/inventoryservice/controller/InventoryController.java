package com.ecommerce.inventoryservice.controller;

import com.ecommerce.inventoryservice.dto.StockRequest;
import com.ecommerce.inventoryservice.dto.StockResponse;
import com.ecommerce.inventoryservice.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<StockResponse>> getAllStock() {
        return ResponseEntity.ok(inventoryService.getAllStock());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<StockResponse> getStock(@PathVariable UUID productId) {
        return ResponseEntity.ok(inventoryService.getStock(productId));
    }

    @PostMapping
    public ResponseEntity<StockResponse> createStock(@RequestBody StockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createStock(request));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<StockResponse> updateStock(
            @PathVariable UUID productId,
            @RequestBody StockRequest request) {
        return ResponseEntity.ok(inventoryService.updateStock(productId, request));
    }

    @PatchMapping("/{productId}/add")
    public ResponseEntity<StockResponse> addStock(
            @PathVariable UUID productId,
            @RequestBody StockRequest request) {
        return ResponseEntity.ok(inventoryService.addStock(productId, request));
    }

    @PatchMapping("/{productId}/deduct")
    public ResponseEntity<StockResponse> deductStock(
            @PathVariable UUID productId,
            @RequestBody StockRequest request) {
        return ResponseEntity.ok(inventoryService.deductStock(productId, request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteStock(@PathVariable UUID productId) {
        inventoryService.deleteStock(productId);
        return ResponseEntity.noContent().build();
    }
}
