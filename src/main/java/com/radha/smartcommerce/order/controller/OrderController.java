package com.radha.smartcommerce.order.controller;

import com.radha.smartcommerce.common.response.ApiResponse;
import com.radha.smartcommerce.order.dto.OrderResponse;
import com.radha.smartcommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ApiResponse<OrderResponse> placeOrder(@AuthenticationPrincipal UserDetails userDetails) {
        OrderResponse orderResponse = orderService.placeOrder(userDetails.getUsername());
        return ApiResponse.<OrderResponse>builder()
                .success(true)
                .message("Order placed successfully")
                .data(orderResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> placeOrder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long orderId) {
        OrderResponse orderResponse = orderService.getOrderById(userDetails.getUsername(), orderId);
        return ApiResponse.<OrderResponse>builder()
                .success(true)
                .message("Order fetched successfully")
                .data(orderResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @GetMapping
    public ApiResponse<Page<OrderResponse>> placeOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size) {
        Page<OrderResponse> orderResponse = orderService.getMyOrders(userDetails.getUsername(), page, size);
        return ApiResponse.<Page<OrderResponse>>builder()
                .success(true)
                .message("Orders fetched successfully")
                .data(orderResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PutMapping("/{orderId}/cancel")
    public ApiResponse<Void> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId
    ) {
        orderService.cancelOrder(
                userDetails.getUsername(),
                orderId
        );

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Order cancelled successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
