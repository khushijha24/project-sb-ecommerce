package com.pro_sb_ecommerce.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddToCartRequest {

    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;
}
