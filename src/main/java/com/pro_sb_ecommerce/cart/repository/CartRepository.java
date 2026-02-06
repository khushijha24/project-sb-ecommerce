package com.pro_sb_ecommerce.cart.repository;

import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

    boolean existsByUser(User user);
}
