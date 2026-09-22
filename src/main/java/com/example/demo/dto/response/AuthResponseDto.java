package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String username;
    private List<String> roles;
}
