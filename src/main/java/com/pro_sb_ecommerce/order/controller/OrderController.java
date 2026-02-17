package com.pro_sb_ecommerce.order.controller;

import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.auth.repository.UserRepository;
import com.pro_sb_ecommerce.cart.service.CartService;
import com.pro_sb_ecommerce.exception.ResourceNotFoundException;
import com.pro_sb_ecommerce.order.dto.OrderResponse;
import com.pro_sb_ecommerce.order.mapper.OrderMapper;
import com.pro_sb_ecommerce.order.model.Order;
import com.pro_sb_ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    private final UserRepository userRepository;

    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // Place Order
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(Authentication authentication){
        User user = getCurrentUser(authentication);

        Order order = orderService.placeOrder(user);

        return ResponseEntity.ok(OrderMapper.toResponse(order));
    }

    // Get My Orders

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication){
        User user = getCurrentUser(authentication);

        List<Order> orders = orderService.getOrdersByUser(user);

        List<OrderResponse> response = orders.stream()
                .map(OrderMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}
