package com.radha.smartcommerce.cart.service;

import com.radha.smartcommerce.auth.entity.User;
import com.radha.smartcommerce.auth.repository.UserRepository;
import com.radha.smartcommerce.cart.dto.AddCartItemRequest;
import com.radha.smartcommerce.cart.dto.CartItemResponse;
import com.radha.smartcommerce.cart.dto.CartResponse;
import com.radha.smartcommerce.cart.dto.UpdateCartItemRequest;
import com.radha.smartcommerce.cart.entity.Cart;
import com.radha.smartcommerce.cart.entity.CartItem;
import com.radha.smartcommerce.cart.repoistory.CartItemRepository;
import com.radha.smartcommerce.cart.repoistory.CartRepository;
import com.radha.smartcommerce.exception.ResourceNotFoundException;
import com.radha.smartcommerce.product.entity.Product;
import com.radha.smartcommerce.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    @Override
    public void addItemToCart(String email, AddCartItemRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Product product = productRepository.findById(request.productId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(request.quantity() + cartItem.getQuantity());
            cartItemRepository.save(cartItem);
        }
        else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.quantity());
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public CartResponse getCart(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        List<CartItemResponse> items = cart.getCartItems()
                .stream().map(item -> {
                    BigDecimal subTotal = item.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                    return new CartItemResponse(
                            item.getId(),
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getProduct().getPrice(),
                            item.getQuantity(),
                            subTotal
                    );
                })
                .toList();
        BigDecimal totalPrice = items.stream()
                .map(CartItemResponse::subtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
        return new CartResponse (cart.getId(), items, totalPrice);
    }

    @Override
    public void updateCartItem(String email, Long cartItemId, UpdateCartItemRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        Cart cart = cartRepository.findByUser(user).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new ResourceNotFoundException("Item not found")
        );
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new AccessDeniedException("You cannot modify another user's cart");
        }
        if (request.quantity() == 0) {
            cartItemRepository.delete(cartItem);
            return;
        }
        cartItem.setQuantity(request.quantity());
        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteCartItem(String email, Long cartItemId) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        Cart cart = cartRepository.findByUser(user).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new ResourceNotFoundException("Item not found")
        );
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new AccessDeniedException("You cannot modify another user's cart");
        }
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void deleteCart(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        Cart cart = cartRepository.findByUser(user).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
