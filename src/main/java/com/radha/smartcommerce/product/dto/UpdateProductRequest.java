package com.radha.smartcommerce.product.dto;

import com.radha.smartcommerce.product.entity.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank(message = "Product name is required")
        String name,
        String description,
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal price,
        @NotNull(message = "Stock quantity is required")
        @Positive(message = "Stock quantity must be positive")
        Integer stockQuantity,
        @NotNull(message = "Category is required")
        Category category
) {
}
