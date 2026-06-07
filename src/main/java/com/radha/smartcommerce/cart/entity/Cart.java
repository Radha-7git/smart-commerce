package com.radha.smartcommerce.cart.entity;

import com.radha.smartcommerce.auth.entity.User;
import com.radha.smartcommerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name="carts")
public class Cart extends BaseEntity {
    @OneToOne
    @JoinColumn(name="user_id", nullable = false, unique = true)
    private User user;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
}
