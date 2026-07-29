package com.ecommerce.shippingservice.service;

import com.ecommerce.shippingservice.dto.*;
import java.util.List;
import java.util.UUID;

public interface ShippingService {
    ShippingResponse createShipping(CreateShippingRequest request);
    ShippingResponse getShippingById(UUID id);
    ShippingResponse getShippingByOrderId(UUID orderId);
    List<ShippingResponse> getShippingByUserId(UUID userId);
    ShippingResponse updateStatus(UUID id, UpdateStatusRequest request);
    ShippingResponse updateTracking(UUID id, UpdateTrackingRequest request);
}
