package com.radha.smartcommerce.cart.entity;

import com.radha.smartcommerce.common.entity.BaseEntity;
import com.radha.smartcommerce.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="cart_items")
public class CartItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(nullable = false)
    private Integer quantity;
}
