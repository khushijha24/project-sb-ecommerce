package com.pro_sb_ecommerce.order.repository;

import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
}

