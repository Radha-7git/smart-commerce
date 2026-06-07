package com.radha.smartcommerce.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
        @NotBlank(message = "Name is Required")
        String name,
        @NotBlank(message = "Email is Required")
        @Email(message= "Invalid email format")
        String email,
        @Size(min = 6, message = "Password must be at least 6 characters")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
                message = "Password must contain uppercase, lowercase, number, and special character"
        )
        String password
){
}
