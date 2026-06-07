package com.radha.smartcommerce.order.entity;

import com.radha.smartcommerce.auth.entity.User;
import com.radha.smartcommerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order extends BaseEntity {
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private BigDecimal totalAmount;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
