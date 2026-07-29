package com.ecommerce.paymentservice.repository;

import com.ecommerce.paymentservice.entity.Payment;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Optional<Payment> findById(UUID id);
    Optional<Payment> findByOrderId(UUID orderId);
    Payment save(Payment payment);
}
