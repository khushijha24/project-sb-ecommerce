package com.pro_sb_ecommerce.cart.mapper;

import com.pro_sb_ecommerce.cart.dto.CartItemResponse;
import com.pro_sb_ecommerce.cart.dto.CartResponse;
import com.pro_sb_ecommerce.cart.model.Cart;
import com.pro_sb_ecommerce.cart.model.CartItem;

import java.math.BigDecimal;
import java.util.List;
public class CartMapper {

    public static CartResponse toResponse(Cart cart) {

        List<CartItemResponse> items = cart.getItems().stream()
                .map(CartMapper::toItemResponse)
                .toList();

        BigDecimal totalAmount = items.stream()
                .map(CartItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                items,
                totalAmount
        );
    }

    private static CartItemResponse toItemResponse(CartItem item) {

        BigDecimal subtotal = item.getPriceAtTime()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getPriceAtTime(),
                item.getQuantity(),
                subtotal
        );
    }

}
