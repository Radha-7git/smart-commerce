package com.radha.smartcommerce.auth.service;

import com.radha.smartcommerce.auth.dto.*;

public interface AuthService {
    RegisterResponse register (RegisterRequest request);
    LoginResponse login(LoginRequest request);
    UserProfileResponse getProfile(String email);
}
