package com.pro_sb_ecommerce.cart.mapper;

import com.pro_sb_ecommerce.cart.dto.CartItemResponse;
import com.pro_sb_ecommerce.cart.dto.CartResponse;
import com.pro_sb_ecommerce.cart.model.Cart;
import com.pro_sb_ecommerce.cart.model.CartItem;

import java.util.List;
public class CartMapper {

    public static CartResponse toResponse(Cart cart) {

        List<CartItemResponse> items = cart.getItems().stream()
                .map(CartMapper::toItemResponse)
                .toList();

        double totalAmount = items.stream()
                .mapToDouble(CartItemResponse::getTotalPrice)
                .sum();

        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                items,
                totalAmount
        );
    }

    private static CartItemResponse toItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getPriceAtTime(),
                item.getQuantity(),
                item.getPriceAtTime() * item.getQuantity()
        );
    }

}
