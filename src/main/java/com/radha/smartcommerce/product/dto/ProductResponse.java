package com.radha.smartcommerce.product.dto;

import com.radha.smartcommerce.product.entity.Category;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        Category category,
        String imageUrl
) {
}
