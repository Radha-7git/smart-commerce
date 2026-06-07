package com.radha.smartcommerce.order.entity;

import com.radha.smartcommerce.common.entity.BaseEntity;
import com.radha.smartcommerce.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name="order_items")
public class OrderItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name="order_id", nullable = false)
    private Order order;
    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private Product product;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private BigDecimal priceAtPurchase;
}
