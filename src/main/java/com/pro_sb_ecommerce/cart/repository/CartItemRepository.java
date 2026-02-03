package com.pro_sb_ecommerce.cart.repository;

import com.pro_sb_ecommerce.cart.model.Cart;
import com.pro_sb_ecommerce.cart.model.CartItem;
import com.pro_sb_ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    void deleteByCart(Cart cart);
}
