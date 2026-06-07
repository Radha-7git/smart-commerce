package com.radha.smartcommerce.auth.controller;

import com.radha.smartcommerce.auth.dto.*;
import com.radha.smartcommerce.auth.service.AuthService;
import com.radha.smartcommerce.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register (@Valid  @RequestBody RegisterRequest request) {
        RegisterResponse registerResponse = authService.register(request);
        return ApiResponse.<RegisterResponse>builder()
                .success (true)
                .message ("Email registered successfully")
                .data (registerResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login (@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login successful")
                .data(loginResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> me(@AuthenticationPrincipal UserDetails userDetails) {
        UserProfileResponse userProfileResponse = authService.getProfile(userDetails.getUsername());
        return ApiResponse.<UserProfileResponse>builder()
                .success(true)
                .message("Profile fetched successfully")
                .data(userProfileResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
