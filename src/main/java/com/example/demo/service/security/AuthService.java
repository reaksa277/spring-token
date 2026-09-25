package com.example.demo.service.security;

import com.example.demo.dto.request.AuthRequestDto;
import com.example.demo.dto.request.RefreshTokenRequestDto;
import com.example.demo.dto.request.RegisterRequestDto;
import com.example.demo.dto.response.AuthResponseDto;
import com.example.demo.dto.response.RefreshTokenResponseDto;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto request);

    AuthResponseDto login(AuthRequestDto payload);

    RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto payload);
}
