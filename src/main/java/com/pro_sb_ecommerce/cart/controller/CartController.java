package com.pro_sb_ecommerce.cart.controller;


import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.auth.repository.UserRepository;
import com.pro_sb_ecommerce.cart.dto.AddToCartRequest;
import com.pro_sb_ecommerce.cart.dto.CartResponse;
import com.pro_sb_ecommerce.cart.service.CartService;
import com.pro_sb_ecommerce.exception.ResourceNotFoundException;
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

    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }


    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName(); // from JWT "sub"

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> viewCart(Authentication authentication) {

        User user = getCurrentUser(authentication);

        CartResponse cart = cartService.viewCart(user);

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK.value())
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
        User user = getCurrentUser(authentication);

        CartResponse cart = cartService.addItemToCart(user, request.getProductId(),
                request.getQuantity());

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK.value())
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

        User user = getCurrentUser(authentication);

        CartResponse cart = cartService.removeItemFromCart(user, productId);

        ApiResponse<CartResponse> response = ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Item removed from cart")
                .data(cart)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<String>> clearCart(Authentication authentication) {

        User user = getCurrentUser(authentication);

        cartService.clearCart(user);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Cart cleared successfully")
                .data("Cart is now empty")
                .build();

        return ResponseEntity.ok(response);
    }
}
