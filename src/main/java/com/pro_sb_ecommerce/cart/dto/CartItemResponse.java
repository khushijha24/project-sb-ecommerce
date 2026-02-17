package com.pro_sb_ecommerce.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CartItemResponse {

    private Long productId;
    private String productName;
    private BigDecimal priceAtTime;
    private int quantity;
    private BigDecimal totalPrice;
}
