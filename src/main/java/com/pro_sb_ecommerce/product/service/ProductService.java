package com.pro_sb_ecommerce.product.service;

import com.pro_sb_ecommerce.product.dto.ProductRequest;
import com.pro_sb_ecommerce.product.dto.ProductResponse;
import com.pro_sb_ecommerce.product.model.Category;
import com.pro_sb_ecommerce.product.model.Product;
import com.pro_sb_ecommerce.product.repository.CategoryRepository;
import com.pro_sb_ecommerce.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // ADMIN
    public ProductResponse addProduct(ProductRequest request) {



        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(category)
                .build();

        Product saved = productRepository.save(product);

        return new ProductResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getPrice(),
                saved.getStock()
        );
    }

    // USER
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStock()
                ))
                .toList();
    }
}

