package com.pro_sb_ecommerce.cart.controller;


import com.pro_sb_ecommerce.cart.dto.CartResponse;
import com.pro_sb_ecommerce.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> viewCart(Authentication authentication) {

        String email = authentication.getName(); // from JWT
        return ResponseEntity.ok(cartService.viewCart(email));
    }
}
