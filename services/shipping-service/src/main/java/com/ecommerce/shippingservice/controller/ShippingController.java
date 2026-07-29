package com.ecommerce.shippingservice.controller;

import com.ecommerce.shippingservice.dto.*;
import com.ecommerce.shippingservice.service.ShippingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    private final ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @PostMapping
    public ResponseEntity<ShippingResponse> createShipping(@RequestBody CreateShippingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shippingService.createShipping(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingResponse> getShippingById(@PathVariable UUID id) {
        return ResponseEntity.ok(shippingService.getShippingById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShippingResponse> getShippingByOrderId(@PathVariable UUID orderId) {
        return ResponseEntity.ok(shippingService.getShippingByOrderId(orderId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ShippingResponse>> getShippingByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(shippingService.getShippingByUserId(userId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShippingResponse> updateStatus(
            @PathVariable UUID id,
            @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(shippingService.updateStatus(id, request));
    }

    @PatchMapping("/{id}/tracking")
    public ResponseEntity<ShippingResponse> updateTracking(
            @PathVariable UUID id,
            @RequestBody UpdateTrackingRequest request) {
        return ResponseEntity.ok(shippingService.updateTracking(id, request));
    }
}
