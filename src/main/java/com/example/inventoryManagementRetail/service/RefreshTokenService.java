package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.exception.RefreshTokenException;
import com.example.inventoryManagementRetail.model.RefreshToken;
import com.example.inventoryManagementRetail.model.User;
import com.example.inventoryManagementRetail.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${app.jwt.refreshTokenExpirationSec}")
    private Long refreshTokenDurationSec;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken RToken = new RefreshToken();
        RToken.setUser(user);
        RToken.setToken(UUID.randomUUID().toString());
        RToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationSec));
        return refreshTokenRepository.save(RToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            log.error("Refresh Token is Expired");
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException("Refresh Token is Expired");
        }
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

}