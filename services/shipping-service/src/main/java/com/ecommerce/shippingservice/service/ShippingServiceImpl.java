package com.ecommerce.shippingservice.service;

import com.ecommerce.shippingservice.dto.*;
import com.ecommerce.shippingservice.entity.Shipment;
import com.ecommerce.shippingservice.entity.ShippingStatus;
import com.ecommerce.shippingservice.repository.ShippingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ShippingServiceImpl implements ShippingService {

    private final ShippingRepository shippingRepository;

    public ShippingServiceImpl(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    @Override
    @Transactional
    public ShippingResponse createShipping(CreateShippingRequest request) {
        if (shippingRepository.findByOrderId(request.orderId()).isPresent()) {
            throw new RuntimeException("Shipping already exists for this order");
        }

        Shipment shipment = Shipment.builder()
                .id(UUID.randomUUID())
                .orderId(request.orderId())
                .userId(request.userId())
                .address(request.address())
                .status(ShippingStatus.PREPARING.name())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return toResponse(shippingRepository.save(shipment));
    }

    @Override
    public ShippingResponse getShippingById(UUID id) {
        Shipment shipment = shippingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
        return toResponse(shipment);
    }

    @Override
    public ShippingResponse getShippingByOrderId(UUID orderId) {
        Shipment shipment = shippingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Shipment not found for this order"));
        return toResponse(shipment);
    }

    @Override
    public List<ShippingResponse> getShippingByUserId(UUID userId) {
        return shippingRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ShippingResponse updateStatus(UUID id, UpdateStatusRequest request) {
        Shipment shipment = shippingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        ShippingStatus curr = ShippingStatus.valueOf(shipment.getStatus());
        ShippingStatus next = ShippingStatus.valueOf(request.status().toUpperCase());

        validateStatusTransition(curr, next);
        shipment.setStatus(next.name());

        if (next == ShippingStatus.SHIPPED) {
            shipment.setShippedAt(LocalDateTime.now());
        }
        if (next == ShippingStatus.DELIVERED) {
            shipment.setDeliveredAt(LocalDateTime.now());
        }

        shipment.setUpdatedAt(LocalDateTime.now());
        return toResponse(shippingRepository.save(shipment));
    }

    @Override
    @Transactional
    public ShippingResponse updateTracking(UUID id, UpdateTrackingRequest request) {
        Shipment shipment = shippingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        shipment.setCarrier(request.carrier());
        shipment.setTrackingNumber(request.trackingNumber());
        shipment.setUpdatedAt(LocalDateTime.now());

        return toResponse(shippingRepository.save(shipment));
    }

    private void validateStatusTransition(ShippingStatus curr, ShippingStatus next) {
        if (curr == ShippingStatus.DELIVERED) {
            throw new RuntimeException("Shipment already delivered");
        }
        if (curr == ShippingStatus.CANCELLED) {
            throw new RuntimeException("Shipment already cancelled");
        }

        boolean valid = switch (curr) {
            case PREPARING -> next == ShippingStatus.SHIPPED || next == ShippingStatus.CANCELLED;
            case SHIPPED -> next == ShippingStatus.IN_TRANSIT || next == ShippingStatus.CANCELLED;
            case IN_TRANSIT -> next == ShippingStatus.DELIVERED || next == ShippingStatus.RETURNED;
            default -> false;
        };

        if (!valid) {
            throw new RuntimeException("Cannot change status from " + curr + " to " + next);
        }
    }

    private ShippingResponse toResponse(Shipment shipment) {
        return new ShippingResponse(
                shipment.getId(),
                shipment.getOrderId(),
                shipment.getUserId(),
                shipment.getAddress(),
                shipment.getStatus(),
                shipment.getCarrier(),
                shipment.getTrackingNumber(),
                shipment.getShippedAt(),
                shipment.getDeliveredAt(),
                shipment.getCreatedAt(),
                shipment.getUpdatedAt()
        );
    }
}
