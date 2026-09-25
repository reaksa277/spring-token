package com.example.demo.service.Impl;

import com.example.demo.common.config.ApplicationConfiguration;
import com.example.demo.exception.model.ResourceNotFoundException;
import com.example.demo.model.entity.RefreshToken;
import com.example.demo.model.entity.UserEntity;
import com.example.demo.respository.RefreshTokenRepository;
import com.example.demo.service.security.RefreshTokenService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ApplicationConfiguration appConfig;

    private long expiration;

    @PostConstruct
    private void init() {
        this.expiration = appConfig.getSecurity().getRefreshTokenExpiration();
    }

    @Override
    public RefreshToken createRefreshToken(UserEntity user) {
        String refreshToken = this.generateSourceRefreshToken();

        log.info("refreshToken expiration value = {}", this.expiration);

        RefreshToken entity = new RefreshToken();
        entity.setToken(refreshToken);
        entity.setExpiresAt(LocalDateTime.now().plusHours(expiration));
        entity.setUser(user);

        return refreshTokenRepository.save(entity);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token is invalid"));
    }

    @Override
    public RefreshToken verifyToken(RefreshToken token) throws AuthenticationException {
        // expiration && revoke
        if (!token.isValid()) {
            refreshTokenRepository.delete(token);
            throw new AuthenticationException("Refresh token was expired or revoked");
        }

        return token;
    }

    @Override
    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {
        // revoke old refresh token
        oldToken.setRevoked(true);

        // generate new refresh token
        return this.createRefreshToken(oldToken.getUser());
    }

    @Override
    @Transactional
    public void deleteExpiredAndRevokedToken() {
        refreshTokenRepository.deleteExpiredAndRevokedToken(LocalDateTime.now());
    }

    private String generateSourceRefreshToken() {
        // -128 to 127
        SecureRandom random = new SecureRandom();

        // array bytes of 64 length, 512 bits
        byte[] tokenBytes = new byte[64];

        // make each byte has its own secure value
        // [67,-125,100,12,....]
        random.nextBytes(tokenBytes);

        // "hwdj1e0slasf3f3/+asjdfasjd/+" not URL friendly
        // "hashfafej_-asdfkjake" URL friendly

        return Base64.getEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
