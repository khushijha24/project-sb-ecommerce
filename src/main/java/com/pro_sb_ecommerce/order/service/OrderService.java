package com.pro_sb_ecommerce.order.service;

import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.cart.model.Cart;
import com.pro_sb_ecommerce.cart.model.CartItem;
import com.pro_sb_ecommerce.cart.repository.CartRepository;
import com.pro_sb_ecommerce.exception.ResourceNotFoundException;
import com.pro_sb_ecommerce.order.model.*;
import com.pro_sb_ecommerce.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    public Order placeOrder(User user) {

        // 1️⃣ Get Cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot place order with empty cart");
        }

        // 2️⃣ Create Order
        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .orderDate(LocalDateTime.now())
                .build();

        // 3️⃣ Convert CartItems → OrderItems
        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productId(cartItem.getProduct().getId())
                    .productName(cartItem.getProduct().getName())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPriceAtTime()) // snapshot
                    .build();

            orderItem.calculateSubtotal();

            order.getOrderItems().add(orderItem);
        }

        // 4️⃣ Calculate total
        order.calculateTotal();

        // 5️⃣ Save order
        Order savedOrder = orderRepository.save(order);

        // 6️⃣ Clear cart
        cart.getItems().clear();

        return savedOrder;
    }
}

