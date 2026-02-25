package com.pro_sb_ecommerce.product.controller;

import com.pro_sb_ecommerce.product.model.Category;
import com.pro_sb_ecommerce.product.repository.CategoryRepository;
import com.pro_sb_ecommerce.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Category>> create(@Valid @RequestBody Category category) {

        Category savedCategory = categoryRepository.save(category);

        ApiResponse<Category> response = ApiResponse.<Category>builder()
                .status(HttpStatus.CREATED.value())
                .message("Category created successfully")
                .data(savedCategory)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAll() {

        List<Category> categories = categoryRepository.findAll();

        ApiResponse<List<Category>> response = ApiResponse.<List<Category>>builder()
                .status(HttpStatus.OK.value())
                .message("Categories fetched successfully")
                .data(categories)
                .build();

        return ResponseEntity.ok(response);
    }
}