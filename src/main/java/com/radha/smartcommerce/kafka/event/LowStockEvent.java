package com.radha.smartcommerce.kafka.event;

public record LowStockEvent(
        Long productId,
        String productName,
        Integer stockRemaining
) {
}
