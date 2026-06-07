package com.radha.smartcommerce.order.service;

import com.radha.smartcommerce.order.dto.OrderResponse;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderResponse placeOrder(String email);
    OrderResponse getOrderById(String email, Long orderId);
    Page<OrderResponse> getMyOrders(String email, int page, int size);
    void cancelOrder(String email, Long orderId);
}
