package com.example.demo.service.security;

import com.example.demo.exception.BusinessException;
import com.example.demo.model.entity.RefreshToken;
import com.example.demo.model.entity.UserEntity;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(UserEntity user);
}
