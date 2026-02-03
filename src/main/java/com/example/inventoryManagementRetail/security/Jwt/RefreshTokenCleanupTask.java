package com.example.inventoryManagementRetail.security.Jwt;

import com.example.inventoryManagementRetail.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
public class RefreshTokenCleanupTask {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenCleanupTask(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Scheduled(fixedDelay = 86400000)
    public void deleteExpiredTokens() {
        Instant cutoffTime = Instant.now().minus(Duration.ofHours(24));
        Integer deleteCount = refreshTokenRepository.deleteExpiredTokensOlderThan(cutoffTime).orElseThrow(() -> new RuntimeException("Failed to delete expired tokens"));
        log.info("Delete {} expired tokens", deleteCount);
    }
}
