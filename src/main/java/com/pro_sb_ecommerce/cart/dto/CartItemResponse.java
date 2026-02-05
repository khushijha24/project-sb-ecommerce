package com.pro_sb_ecommerce.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemResponse {

    private Long productId;
    private String productName;
    private double priceAtTime;
    private int quantity;
    private double totalPrice;
}
