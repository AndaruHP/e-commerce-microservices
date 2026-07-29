package com.ecommerce.shippingservice.repository;

import com.ecommerce.shippingservice.entity.Shipment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShippingRepository {
    Optional<Shipment> findById(UUID id);
    Optional<Shipment> findByOrderId(UUID orderId);
    List<Shipment> findByUserId(UUID userId);
    Shipment save(Shipment shipment);
}
