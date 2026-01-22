package com.pro_sb_ecommerce.product.controller;

import com.pro_sb_ecommerce.product.dto.ProductRequest;
import com.pro_sb_ecommerce.product.dto.ProductResponse;
import com.pro_sb_ecommerce.product.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 👑 ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductResponse addProduct(@RequestBody ProductRequest request) {
        return productService.addProduct(request);
    }

    // 👤 USER ONLY
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }
}

