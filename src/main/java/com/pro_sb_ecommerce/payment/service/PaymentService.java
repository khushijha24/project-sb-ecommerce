package com.pro_sb_ecommerce.payment.service;

import com.pro_sb_ecommerce.exception.ResourceNotFoundException;
import com.pro_sb_ecommerce.order.model.Order;
import com.pro_sb_ecommerce.order.model.OrderStatus;
import com.pro_sb_ecommerce.order.repository.OrderRepository;
import com.pro_sb_ecommerce.payment.dto.PaymentRequest;
import com.pro_sb_ecommerce.payment.dto.PaymentResponse;
import com.pro_sb_ecommerce.payment.mapper.PaymentMapper;
import com.pro_sb_ecommerce.payment.model.Payment;
import com.pro_sb_ecommerce.payment.model.PaymentStatus;
import com.pro_sb_ecommerce.payment.repository.PaymentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    private final Random random = new Random();

    public PaymentService(OrderRepository orderRepository,
                          PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse processPayment(PaymentRequest request,
                                          Authentication authentication) {

        // 1️⃣ Fetch Order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // 2️⃣ Ownership Validation
        String email = authentication.getName();
        if (!order.getUser().getEmail().equals(email)) {
            throw new IllegalStateException("You cannot pay for this order");
        }

        // 3️⃣ Order State Validation
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Order is not eligible for payment");
        }

        // 4️⃣ Get Existing Payment (OneToOne)
        Payment payment = paymentRepository.findByOrder(order).orElse(null);

        // 5️⃣ Block if already paid
        if (payment != null && payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Order already paid");
        }

        // 6️⃣ Create Payment if not exists
        if (payment == null) {
            payment = Payment.builder()
                    .order(order)
                    .amount(order.getTotalAmount())
                    .build();
        }

        // 7️⃣ Always update attempt details (for retry support)
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionId("TXN-" + UUID.randomUUID());
        payment.setPaymentDate(LocalDateTime.now());

        // 8️⃣ Simulate Payment Result
        boolean success = random.nextBoolean();

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.PAID);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            // Order remains PENDING (retry allowed)
        }

        // 9️⃣ Save Payment (Order auto-updated via dirty checking)
        paymentRepository.save(payment);

        return PaymentMapper.toResponse(payment);
    }

    public PaymentResponse getPaymentByOrder(Long orderId,
                                             Authentication authentication) {

        // 1️⃣ Fetch Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // 2️⃣ Ownership Validation
        String email = authentication.getName();
        if (!order.getUser().getEmail().equals(email)) {
            throw new IllegalStateException("Access denied");
        }

        // 3️⃣ Fetch Payment
        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        return PaymentMapper.toResponse(payment);
    }
}