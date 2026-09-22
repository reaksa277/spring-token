package com.example.demo.controller;

import com.example.demo.dto.Response;
import com.example.demo.dto.request.AuthRequestDto;
import com.example.demo.dto.response.AuthResponseDto;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<AuthResponseDto>> register(@Valid @RequestBody AuthRequestDto payload) {
        AuthResponseDto dto = authService.register(payload);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("201", "success", "User registered", dto));
    }
}
