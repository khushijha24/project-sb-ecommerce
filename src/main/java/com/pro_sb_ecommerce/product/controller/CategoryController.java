package com.pro_sb_ecommerce.product.controller;

import com.pro_sb_ecommerce.product.model.Category;
import com.pro_sb_ecommerce.product.repository.CategoryRepository;
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
    public Category create(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}

