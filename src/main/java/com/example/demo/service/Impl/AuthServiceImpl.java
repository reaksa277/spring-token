package com.example.demo.service.Impl;

import com.example.demo.dto.request.AuthRequestDto;
import com.example.demo.dto.response.AuthResponseDto;
import com.example.demo.exception.model.UsernameAlreadyTakenException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.entity.RefreshToken;
import com.example.demo.model.entity.UserEntity;
import com.example.demo.respository.UserRepository;
import com.example.demo.service.security.AuthService;
import com.example.demo.service.security.JwtUtil;
import com.example.demo.service.security.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public AuthResponseDto register(AuthRequestDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyTakenException(request.getUsername());
        }

        UserEntity user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity createdUser = userRepository.save(user);

        String accessToken = jwtUtil.generateToken(createdUser);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(createdUser);
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new AuthResponseDto(accessToken, refreshToken.getToken(), user.getUsername(), roles);
    }
}
