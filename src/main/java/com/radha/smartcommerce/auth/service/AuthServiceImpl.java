package com.radha.smartcommerce.auth.service;

import com.radha.smartcommerce.auth.dto.*;
import com.radha.smartcommerce.auth.entity.User;
import com.radha.smartcommerce.auth.mapper.AuthMapper;
import com.radha.smartcommerce.auth.repository.UserRepository;
import com.radha.smartcommerce.exception.InvalidCredentialsException;
import com.radha.smartcommerce.exception.ResourceAlreadyExistsException;
import com.radha.smartcommerce.exception.ResourceNotFoundException;
import com.radha.smartcommerce.security.jwt.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final JwtService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        User user = authMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);
        return new RegisterResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new  InvalidCredentialsException ("Email does not exist"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token);
    }

    @Override
    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserProfileResponse(user.getName(), user.getEmail(), user.getRole().name());
    }
}
