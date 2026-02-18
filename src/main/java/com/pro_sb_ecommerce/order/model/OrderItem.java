package com.pro_sb_ecommerce.order.model;

import com.pro_sb_ecommerce.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private Long productId;
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;  // Snapshot at order time

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    public void calculateSubtotal() {
        this.subtotal = price.multiply(BigDecimal.valueOf(quantity));;
    }
}
