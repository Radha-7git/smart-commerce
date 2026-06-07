package com.radha.smartcommerce.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;


public record UpdateCartItemRequest(
        @NotNull
        @PositiveOrZero
        Integer quantity
) {
}
