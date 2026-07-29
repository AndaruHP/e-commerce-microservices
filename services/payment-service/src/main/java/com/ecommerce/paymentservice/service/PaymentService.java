package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.dto.CreatePaymentRequest;
import com.ecommerce.paymentservice.dto.PaymentResponse;
import com.ecommerce.paymentservice.dto.UpdatePaymentStatusRequest;
import java.util.UUID;

public interface PaymentService {
    PaymentResponse createPayment(CreatePaymentRequest request);
    PaymentResponse getPaymentById(UUID id);
    PaymentResponse getPaymentByOrderId(UUID orderId);
    PaymentResponse updateStatus(UUID id, UpdatePaymentStatusRequest request);
    PaymentResponse refundPayment(UUID id);
}
