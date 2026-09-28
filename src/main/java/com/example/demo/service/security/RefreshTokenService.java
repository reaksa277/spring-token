package com.example.demo.service.security;

import com.example.demo.model.entity.RefreshToken;
import com.example.demo.model.entity.UserEntity;

import javax.naming.AuthenticationException;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(UserEntity user);

    RefreshToken findByToken(String token);

    RefreshToken verifyToken(RefreshToken token) throws AuthenticationException;

    RefreshToken rotateRefreshToken(RefreshToken oldToken);

    void deleteExpiredAndRevokedToken ();
}
