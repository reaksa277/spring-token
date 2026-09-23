package com.example.demo.service.security;

import com.example.demo.dto.request.AuthRequestDto;
import com.example.demo.dto.response.AuthResponseDto;

public interface AuthService {
    AuthResponseDto register(AuthRequestDto request);
}
