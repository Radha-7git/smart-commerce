package com.radha.smartcommerce.exception.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(boolean success, String message, Object errors, LocalDateTime timestamp) {
}
