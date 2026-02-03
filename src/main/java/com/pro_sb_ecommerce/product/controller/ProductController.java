package com.pro_sb_ecommerce.product.controller;

import com.pro_sb_ecommerce.product.dto.ProductRequest;
import com.pro_sb_ecommerce.product.dto.ProductResponse;
import com.pro_sb_ecommerce.product.service.ProductService;
import com.pro_sb_ecommerce.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.addProduct(request);

        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Product Created Successfully")
                .data(product)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // USER ONLY
//    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {

        List<ProductResponse> products = productService.getAllProducts();

        ApiResponse<List<ProductResponse>> response= ApiResponse.<List<ProductResponse>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Products Successfully Fetched")
                .data(products)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {

        ProductResponse product = productService.getProductById(id);

        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product Fetched Successfully")
                .data(product)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getByCategory(@PathVariable Long categoryId) {
        List<ProductResponse> products = productService.getProductsByCategory(categoryId);

        ApiResponse<List<ProductResponse>> response = ApiResponse.<List<ProductResponse>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Products fetched successfully for category" + categoryId)
                .data(products)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ) {
        ProductResponse updatedProduct = productService.updateProduct(id, request);

        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product Updated Successfully")
                .data(updatedProduct)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .success(true)
                .message("Product Successfully Deleted")
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }


    //Pagination Api
//    @GetMapping("/paged")
//    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProductsPaged(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "asc") String sortDir
//    ) {
//        Page<ProductResponse> products =
//                productService.getAllProductsPaginated(page, size, sortBy, sortDir);
//
//        ApiResponse<Page<ProductResponse>> response =
//                ApiResponse.<Page<ProductResponse>>builder()
//                        .status(HttpStatus.OK.value())
//                        .success(true)
//                        .message("Products fetched successfully")
//                        .data(products)
//                        .build();
//
//        return ResponseEntity.ok(response);
//    }


    @GetMapping("/sorted")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProductsSorted(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        List<ProductResponse> products =
                productService.getAllProductsSorted(sortBy, sortDir);

        ApiResponse<List<ProductResponse>> response =
                ApiResponse.<List<ProductResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Products sorted successfully")
                        .data(products)
                        .build();

        return ResponseEntity.ok(response);
    }



    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(
            @RequestParam String keyword
    ) {
        List<ProductResponse> products =
                productService.searchByName(keyword);

        ApiResponse<List<ProductResponse>> response =
                ApiResponse.<List<ProductResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Search results")
                        .data(products)
                        .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchByPrice(
            @RequestParam double min,
            @RequestParam double max
    ) {
        List<ProductResponse> products =
                productService.searchByPriceRange(min, max);

        ApiResponse<List<ProductResponse>> response =
                ApiResponse.<List<ProductResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Products in price range")
                        .data(products)
                        .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{id}/sorted")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getByCategorySorted(
            @PathVariable Long id,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        List<ProductResponse> products =
                productService.getByCategorySorted(id, sortBy, sortDir);

        ApiResponse<List<ProductResponse>> response =
                ApiResponse.<List<ProductResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Category products sorted")
                        .data(products)
                        .build();

        return ResponseEntity.ok(response);
    }

}

