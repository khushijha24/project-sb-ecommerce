package com.pro_sb_ecommerce.product.repository;

import com.pro_sb_ecommerce.product.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByPriceBetween(double min, double max);

    List<Product> findByCategoryId(Long categoryId, Sort sort);
}
