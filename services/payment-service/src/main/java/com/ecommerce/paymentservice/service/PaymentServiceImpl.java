package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.client.OrderServiceClient;
import com.ecommerce.paymentservice.dto.CreatePaymentRequest;
import com.ecommerce.paymentservice.dto.PaymentResponse;
import com.ecommerce.paymentservice.dto.UpdatePaymentStatusRequest;
import com.ecommerce.paymentservice.entity.Payment;
import com.ecommerce.paymentservice.entity.PaymentStatus;
import com.ecommerce.paymentservice.event.PaymentCompletedEvent;
import com.ecommerce.paymentservice.event.PaymentEventPublisher;
import com.ecommerce.paymentservice.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderServiceClient orderServiceClient;
    private final PaymentEventPublisher paymentEventPublisher;

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        if (paymentRepository.findByOrderId(request.orderId()).isPresent()) {
            throw new RuntimeException("Payment already exists for this order");
        }

        Map<String, Object> order = orderServiceClient.getOrder(request.orderId());
        BigDecimal amount = new BigDecimal(order.get("totalPrice").toString());

        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .orderId(request.orderId())
                .userId(request.userId())
                .amount(amount)
                .status(PaymentStatus.PENDING.name())
                .paymentMethod(request.paymentMethod())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return toResponse(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponse getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return toResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for this order"));
        return toResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse updateStatus(UUID id, UpdatePaymentStatusRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        PaymentStatus currentStatus = PaymentStatus.valueOf(payment.getStatus());
        PaymentStatus newStatus = PaymentStatus.valueOf(request.status().toUpperCase());

        validateStatusTransition(currentStatus, newStatus);

        payment.setStatus(newStatus.name());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        if (newStatus == PaymentStatus.COMPLETED) {
            PaymentCompletedEvent event = new PaymentCompletedEvent(
                    payment.getId(),
                    payment.getOrderId(),
                    payment.getUserId(),
                    payment.getAmount(),
                    LocalDateTime.now()
            );

            paymentEventPublisher.paymentCompleted(event);
        }

        return toResponse(saved);
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getStatus().equals(PaymentStatus.COMPLETED.name())) {
            throw new RuntimeException("Only completed payments can be refunded");
        }

        payment.setStatus(PaymentStatus.REFUNDED.name());
        payment.setUpdatedAt(LocalDateTime.now());
        return toResponse(paymentRepository.save(payment));
    }

    private void validateStatusTransition(PaymentStatus current, PaymentStatus next) {
        boolean valid = switch (current) {
            case PENDING -> next == PaymentStatus.COMPLETED || next == PaymentStatus.FAILED;
            case COMPLETED -> false;
            case FAILED -> next == PaymentStatus.PENDING;
            case REFUNDED -> false;
        };

        if (!valid) {
            throw new RuntimeException("Cannot change status from " + current + " to " + next);
        }
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
