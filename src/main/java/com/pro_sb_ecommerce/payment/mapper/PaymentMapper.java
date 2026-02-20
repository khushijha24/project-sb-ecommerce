package com.pro_sb_ecommerce.payment.mapper;

import com.pro_sb_ecommerce.payment.dto.PaymentResponse;
import com.pro_sb_ecommerce.payment.model.Payment;

public class PaymentMapper {

    public static PaymentResponse toResponse(Payment payment){
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getPaymentDate(),
                payment.getPaymentMethod()
        );
    }
}
