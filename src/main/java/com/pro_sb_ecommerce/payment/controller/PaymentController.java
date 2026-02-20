package com.pro_sb_ecommerce.payment.controller;

import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.auth.repository.UserRepository;
import com.pro_sb_ecommerce.exception.ResourceNotFoundException;
import com.pro_sb_ecommerce.payment.dto.PaymentRequest;
import com.pro_sb_ecommerce.payment.dto.PaymentResponse;
import com.pro_sb_ecommerce.payment.model.Payment;
import com.pro_sb_ecommerce.payment.service.PaymentService;
import com.pro_sb_ecommerce.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @Valid @RequestBody
            PaymentRequest request,
            Authentication authentication){

        PaymentResponse response = paymentService.processPayment(request, authentication);

        ApiResponse<PaymentResponse> apiResponse =
                ApiResponse.<PaymentResponse>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Payment processed successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByOrder(
            @PathVariable Long orderId,
            Authentication authentication) {

        PaymentResponse response =
                paymentService.getPaymentByOrder(orderId, authentication);

        ApiResponse<PaymentResponse> apiResponse =
                ApiResponse.<PaymentResponse>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Payment processed successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
}
