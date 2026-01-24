package com.artclassmanagement.service.impl;

import com.artclassmanagement.dto.request.LoginRequest;
import com.artclassmanagement.dto.request.RefreshTokenRequest;
import com.artclassmanagement.dto.request.RegisterRequest;
import com.artclassmanagement.dto.response.AuthenticationResponse;
import com.artclassmanagement.dto.response.RegistrationResponse;
import com.artclassmanagement.entity.RefreshToken;
import com.artclassmanagement.entity.User;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.repository.UserRepository;
import com.artclassmanagement.security.JwtService;
import com.artclassmanagement.service.AuthenticationService;
import com.artclassmanagement.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    private static final String DEFAULT_ROLE = "ROLE_CUSTOMER";

    @Override
    @Transactional
    public RegistrationResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check if email already exists
        if (userRepository.findByEmailAndDeletedFalse(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        // Create new user with default role
        Set<String> roles = new HashSet<>();
        roles.add(DEFAULT_ROLE);

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .roles(roles)
                .isEnabled(true) // Enable immediately (no OTP verification)
                .deleted(false)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return RegistrationResponse.builder()
                .success(true)
                .message("Registration successful. You can now login.")
                .userId(savedUser.getId())
                .build();
    }

    @Override
    @Transactional
    public AuthenticationResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // Authenticate using Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        // Get user
        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account is not enabled. Please contact support.");
        }

        // Generate tokens
        return buildAuthenticationResponse(user);
    }

    @Override
    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        log.info("Refreshing token");

        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        // Verify token is not expired
        refreshTokenService.verifyExpiration(refreshToken);

        // Get user
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Generate new access token
        String accessToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(jwtService.getExpirationTime())
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(user.getRoles())
                .build();
    }

    private AuthenticationResponse buildAuthenticationResponse(User user) {
        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(jwtService.getExpirationTime())
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(user.getRoles())
                .build();
    }
}
