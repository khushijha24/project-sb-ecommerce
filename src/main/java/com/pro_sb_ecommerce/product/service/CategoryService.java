package com.pro_sb_ecommerce.product.service;

import com.pro_sb_ecommerce.exception.ResourceNotFoundException;
import com.pro_sb_ecommerce.product.dto.CategoryRequest;
import com.pro_sb_ecommerce.product.dto.CategoryResponse;
import com.pro_sb_ecommerce.product.dto.ProductRequest;
import com.pro_sb_ecommerce.product.dto.ProductResponse;
import com.pro_sb_ecommerce.product.model.Category;
import com.pro_sb_ecommerce.product.model.Product;
import com.pro_sb_ecommerce.product.repository.CategoryRepository;
import com.pro_sb_ecommerce.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse addCategory(CategoryRequest request) {

//        Category category = categoryRepository.findByName(request.getCategoryName())
//                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category saved = categoryRepository.save(category);

        return new CategoryResponse(
                saved.getId(),
                saved.getName()
        );
    }
}
