package com.radha.smartcommerce.cart.service;

import com.radha.smartcommerce.cart.dto.AddCartItemRequest;
import com.radha.smartcommerce.cart.dto.CartResponse;
import com.radha.smartcommerce.cart.dto.UpdateCartItemRequest;

public interface CartService {
    void addItemToCart(String email, AddCartItemRequest request);
    CartResponse getCart(String email);
    void updateCartItem(String email, Long cartItemId, UpdateCartItemRequest request);
    void deleteCartItem(String email, Long cartItemId);
    void deleteCart(String email);
}
