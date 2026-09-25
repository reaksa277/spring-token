package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RefreshTokenResponseDto extends AuthResponseDto{

    @JsonProperty("token_type")
    private String tokenType;

    public RefreshTokenResponseDto(String accessToken, String refreshToken, String username, List<String> roles, String tokenType) {
        super(accessToken, refreshToken, username, roles);
        this.tokenType = tokenType;
    }
}
