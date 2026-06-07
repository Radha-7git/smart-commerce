package com.radha.smartcommerce.product.repository;

import com.radha.smartcommerce.product.entity.Category;
import com.radha.smartcommerce.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase (String name, Pageable pageable);
}
