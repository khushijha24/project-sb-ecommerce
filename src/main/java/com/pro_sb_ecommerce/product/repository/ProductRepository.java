package com.pro_sb_ecommerce.product.repository;

import com.pro_sb_ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
