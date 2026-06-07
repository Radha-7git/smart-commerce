package com.radha.smartcommerce.kafka.event;

public record OrderCancelledEvent(Long userId, Long orderId) {

}
