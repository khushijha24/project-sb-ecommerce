package com.pro_sb_ecommerce.order.controller;

import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.auth.repository.UserRepository;
import com.pro_sb_ecommerce.exception.ResourceNotFoundException;
import com.pro_sb_ecommerce.order.dto.OrderResponse;
import com.pro_sb_ecommerce.order.mapper.OrderMapper;
import com.pro_sb_ecommerce.order.model.Order;
import com.pro_sb_ecommerce.order.service.OrderService;
import com.pro_sb_ecommerce.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(Authentication authentication){
        User user = getCurrentUser(authentication);

        Order order = orderService.placeOrder(user);

        OrderResponse orderResponse = OrderMapper.toResponse(order);

        ApiResponse<OrderResponse> response =
                ApiResponse.<OrderResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .success(true)
                        .message("Order placed successfully")
                        .data(orderResponse)
                        .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get My Orders

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(Authentication authentication){
        User user = getCurrentUser(authentication);

        List<Order> orders = orderService.getOrdersByUser(user);

        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderMapper::toResponse)
                .toList();

        ApiResponse<List<OrderResponse>> response =
                ApiResponse.<List<OrderResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Orders fetched successfully")
                        .data(orderResponses)
                        .build();

        return ResponseEntity.ok(response);
    }
}
