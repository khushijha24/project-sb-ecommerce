package com.pro_sb_ecommerce.payment.repository;

import com.pro_sb_ecommerce.order.model.Order;
import com.pro_sb_ecommerce.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);

    boolean existsByOrder(Order order);
}
