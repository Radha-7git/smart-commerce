package com.radha.smartcommerce.order.repository;

import com.radha.smartcommerce.auth.entity.User;
import com.radha.smartcommerce.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByUser(User user, Pageable pageable);
}
