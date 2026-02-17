package com.pro_sb_ecommerce.order.mapper;

import com.pro_sb_ecommerce.order.dto.OrderItemResponse;
import com.pro_sb_ecommerce.order.dto.OrderResponse;
import com.pro_sb_ecommerce.order.model.Order;
import com.pro_sb_ecommerce.order.model.OrderItem;

import java.util.List;

public class OrderMapper {

    private OrderMapper() {
    }

    // Map Order → OrderResponse
    public static OrderResponse toResponse(Order order){
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(OrderMapper::mapToOrderItemResponse)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                items
        );
    }

    // Map OrderItem → OrderItemResponse
    private static OrderItemResponse mapToOrderItemResponse(OrderItem orderItem){
        return new OrderItemResponse(
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getSubtotal()
        );
    }
}
