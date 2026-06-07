package com.radha.smartcommerce.order.dto;

import com.radha.smartcommerce.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Long orderId,
        BigDecimal totalAmount,
        OrderStatus status,
        List<OrderItemResponse> items
) {
}
