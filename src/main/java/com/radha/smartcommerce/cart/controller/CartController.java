package com.radha.smartcommerce.cart.controller;

import com.radha.smartcommerce.cart.dto.AddCartItemRequest;
import com.radha.smartcommerce.cart.dto.CartResponse;
import com.radha.smartcommerce.cart.dto.UpdateCartItemRequest;
import com.radha.smartcommerce.cart.service.CartService;
import com.radha.smartcommerce.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @PostMapping("/items")
    public ApiResponse<Void> addItemToCart(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody AddCartItemRequest request) {
        cartService.addItemToCart(userDetails.getUsername(), request);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Item added to cart successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @GetMapping
    public ApiResponse<CartResponse> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        CartResponse cartResponse = cartService.getCart(userDetails.getUsername());
        return ApiResponse.<CartResponse>builder()
                .success(true)
                .message("Item added to cart successfully")
                .data(cartResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PutMapping("/items/{cartItemId}")
    public ApiResponse<Void> updateCartItem (@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long cartItemId, @Valid @RequestBody UpdateCartItemRequest request) {
        cartService.updateCartItem(userDetails.getUsername(), cartItemId, request);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Item added to cart successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @DeleteMapping("/items/{cartItemId}")
    public ApiResponse<Void> deleteCartItem (@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long cartItemId) {
        cartService.deleteCartItem(userDetails.getUsername(), cartItemId);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Item deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @DeleteMapping
    public ApiResponse<Void> deleteCart (@AuthenticationPrincipal UserDetails userDetails) {
        cartService.deleteCart(userDetails.getUsername());
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Cart cleared successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
