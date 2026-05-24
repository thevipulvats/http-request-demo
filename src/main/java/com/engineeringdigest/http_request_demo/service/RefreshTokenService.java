package com.engineeringdigest.http_request_demo.service;


import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.model.RefreshToken;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        refreshToken.setRevoked(false);
        log.info("Creating refresh token for user: {}", user.getName());
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken rotateRefreshToken(RefreshToken oldRefreshToken) {
        oldRefreshToken.setRevoked(true);
        refreshTokenRepository.save(oldRefreshToken);
        log.info("Rotating refresh token for user: {}", oldRefreshToken.getUser().getName());
        return createRefreshToken(oldRefreshToken.getUser());
    }


    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));
        if (refreshToken.isRevoked()) {
            log.warn("Revoked refresh token used by user: {}", refreshToken.getUser().getName());
            throw new RuntimeException("Refresh token has been revoked");
        }
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            log.warn("Expired refresh token deleted for user: {}", refreshToken.getUser().getName());
            throw new RuntimeException("Refresh token has expired. Please login again.");
        }
        return refreshToken;
    }


    @Transactional
    public void logoutByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> revokeAllUserTokens(rt.getUser()));
    }


    public void revokeAllUserTokens(User user) {
        log.info("Revoking all refresh tokens for user: {}", user.getName());
        refreshTokenRepository.deleteByUser(user);
    }


}
