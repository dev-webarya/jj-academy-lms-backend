package com.artclassmanagement.service.impl;

import com.artclassmanagement.entity.RefreshToken;
import com.artclassmanagement.entity.User;
import com.artclassmanagement.exception.ResourceNotFoundException;
import com.artclassmanagement.repository.RefreshTokenRepository;
import com.artclassmanagement.repository.UserRepository;
import com.artclassmanagement.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshTokenDurationMs;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String userId) {
        // Delete existing refresh tokens for this user
        refreshTokenRepository.deleteByUserId(userId);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token has expired. Please login again.");
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteByUserId(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
        log.info("Deleted refresh tokens for user ID: {}", userId);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        User user = userRepository.findByEmailAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));
        refreshTokenRepository.deleteByUserId(user.getId());
        log.info("Deleted refresh tokens for username: {}", username);
    }
}
