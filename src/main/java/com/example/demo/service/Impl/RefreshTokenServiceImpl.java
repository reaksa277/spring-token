package com.example.demo.service.Impl;

import com.example.demo.common.config.ApplicationConfiguration;
import com.example.demo.model.entity.RefreshToken;
import com.example.demo.model.entity.UserEntity;
import com.example.demo.respository.RefreshTokenRepository;
import com.example.demo.service.security.RefreshTokenService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
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

        RefreshToken entity = new RefreshToken();
        entity.setToken(refreshToken);
        entity.setExpiresAt(LocalDateTime.now().plusHours(expiration));
        entity.setUser(user);

        return refreshTokenRepository.save(entity);
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
