package com.radha.smartcommerce.order.service;

import com.radha.smartcommerce.auth.entity.User;
import com.radha.smartcommerce.auth.repository.UserRepository;
import com.radha.smartcommerce.cart.entity.Cart;
import com.radha.smartcommerce.cart.entity.CartItem;
import com.radha.smartcommerce.cart.repoistory.CartRepository;
import com.radha.smartcommerce.exception.ResourceNotFoundException;
import com.radha.smartcommerce.kafka.event.LowStockEvent;
import com.radha.smartcommerce.kafka.event.OrderCancelledEvent;
import com.radha.smartcommerce.kafka.event.OrderCreatedEvent;
import com.radha.smartcommerce.kafka.producer.OrderEventProducer;
import com.radha.smartcommerce.order.dto.OrderItemResponse;
import com.radha.smartcommerce.order.dto.OrderResponse;
import com.radha.smartcommerce.order.entity.Order;
import com.radha.smartcommerce.order.entity.OrderItem;
import com.radha.smartcommerce.order.entity.OrderStatus;
import com.radha.smartcommerce.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    @Override
    public OrderResponse placeOrder(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        Cart cart = cartRepository.findByUser(user).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot place order with empty cart"
            );
        }
        List<CartItem> items = cart.getCartItems();
        for (CartItem item:items) {
            if (item.getQuantity() > item.getProduct().getStockQuantity()) {
                throw new IllegalStateException(
                        "Insufficient stock for product: "
                                + item.getProduct().getName()
                );
            }
        }
        Order order = new Order();
        order.setUser(user);
        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item:items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setOrder(order);
            item.getProduct().setStockQuantity(item.getProduct().getStockQuantity() - item.getQuantity());
            if (item.getProduct().getStockQuantity() < 5) {
                LowStockEvent lowStockEvent = new LowStockEvent(item.getProduct().getId(), item.getProduct().getName(), item.getProduct().getStockQuantity());
                orderEventProducer.sendLowStockEvent(lowStockEvent);
            }
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPriceAtPurchase(item.getProduct().getPrice());
            BigDecimal subtotal =
                    item.getProduct()
                            .getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            item.getQuantity()
                                    )
                            );

            totalAmount = totalAmount.add(subtotal);
            orderItemResponses.add(new OrderItemResponse(orderItem.getProduct().getId(),orderItem.getProduct().getName(), orderItem.getQuantity(), orderItem.getPriceAtPurchase(), subtotal));
            orderItems.add(orderItem);
        }

        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
        OrderCreatedEvent event = new OrderCreatedEvent(order.getId(), order.getUser().getId(), order.getTotalAmount());
        orderEventProducer.sendOrderCreatedEvent(event);
        cart.getCartItems().clear();
        return new OrderResponse(order.getId(), order.getTotalAmount(), order.getOrderStatus(), orderItemResponses);
    }

    @Override
    public OrderResponse getOrderById(String email, Long orderId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Cannot access another user's order");
        }
        return mapToOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getMyOrders(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        Page<Order> orders = orderRepository.findByUser(user, pageable);
        return orders.map(this::mapToOrderResponse);
    }

    @Override
    public void cancelOrder(String email, Long orderId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) throw new AccessDeniedException("Cannot access another user's order");
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending orders can be cancelled"
            );
        }
        List<OrderItem> items = order.getOrderItems();
        for (OrderItem item:items) {
            item.getProduct().setStockQuantity(item.getProduct().getStockQuantity() + item.getQuantity());
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        OrderCancelledEvent event = new OrderCancelledEvent(order.getId(), user.getId());
        orderEventProducer.sendOrderCancelledEvent(event);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem item : orderItems) {
            BigDecimal subtotal = item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity()));
            orderItemResponses.add(new OrderItemResponse(item.getProduct().getId(), item.getProduct().getName(), item.getQuantity(), item.getPriceAtPurchase(), subtotal));
        }
        return new OrderResponse(order.getId(), order.getTotalAmount(), order.getOrderStatus(), orderItemResponses);
    }
}
