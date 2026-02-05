package com.pro_sb_ecommerce.cart.controller;


import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.cart.dto.AddToCartRequest;
import com.pro_sb_ecommerce.cart.dto.CartResponse;
import com.pro_sb_ecommerce.cart.service.CartService;
import com.pro_sb_ecommerce.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> viewCart(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        CartResponse cart = cartService.viewCart(user);

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Cart fetched successfully")
                .data(cart)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            Authentication authentication,
            @Valid @RequestBody AddToCartRequest request
    ) {

        User user = (User) authentication.getPrincipal();

        CartResponse cart = cartService.addItemToCart(user, request.getProductId(),
                request.getQuantity());

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Item added to cart")
                .data(cart)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<CartResponse>> removeFromCart(
            Authentication authentication,
            @RequestParam Long productId
    ) {

        User user = (User) authentication.getPrincipal();

        CartResponse cart = cartService.removeItemFromCart(user, productId);

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Item removed from cart")
                .data(cart)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<String>> clearCart(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        cartService.clearCart(user);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Cart cleared successfully")
                .data("Cart is now empty")
                .build();

        return ResponseEntity.ok(response);
    }
}
