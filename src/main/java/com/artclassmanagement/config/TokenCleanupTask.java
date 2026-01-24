package com.artclassmanagement.config;

import com.artclassmanagement.repository.RefreshTokenRepository;
import com.artclassmanagement.repository.TokenDenyListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Scheduled task to clean up expired tokens from the database.
 * Runs daily at midnight to remove expired refresh tokens and denied JWTs.
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupTask {

    private final TokenDenyListRepository tokenDenyListRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Starting token cleanup task...");

        Instant now = Instant.now();

        // Delete expired denied tokens
        tokenDenyListRepository.deleteByExpiryDateBefore(now);
        log.info("Cleaned up expired entries from token deny list.");

        // Delete expired refresh tokens
        refreshTokenRepository.deleteByExpiryDateBefore(now);
        log.info("Cleaned up expired refresh tokens.");

        log.info("Token cleanup task completed.");
    }
}
