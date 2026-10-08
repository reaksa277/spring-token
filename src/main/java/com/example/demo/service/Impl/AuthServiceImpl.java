package com.example.demo.service.Impl;

import com.example.demo.dto.request.AuthRequestDto;
import com.example.demo.dto.request.RefreshTokenRequestDto;
import com.example.demo.dto.request.RegisterRequestDto;
import com.example.demo.dto.response.AuthResponseDto;
import com.example.demo.dto.response.RefreshTokenResponseDto;
import com.example.demo.exception.model.CustomAuthenticationException;
import com.example.demo.exception.model.UsernameAlreadyTakenException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.entity.RefreshToken;
import com.example.demo.model.entity.UserEntity;
import com.example.demo.respository.UserRepository;
import com.example.demo.service.security.AuthService;
import com.example.demo.service.security.JwtUtil;
import com.example.demo.service.security.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyTakenException(request.getUsername());
        }

        UserEntity user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setCreatedBy("system");

        UserEntity createdUser = userRepository.save(user);

        String accessToken = jwtUtil.generateToken(createdUser);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(createdUser);
        List<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.name())
                .collect(Collectors.toList());

        return new AuthResponseDto(accessToken, refreshToken.getToken(), user.getUsername(), roles);
    }

    @Override
    public AuthResponseDto login(AuthRequestDto payload) {
         try {
             // login user
             authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(payload.getUsername(), payload.getPassword())
             );
         } catch (BadCredentialsException e) {
             throw new CustomAuthenticationException("Invalid username or password");
         }

        UserDetails userDetails = userService.loadUserByUsername(payload.getUsername());

        String accessToken = jwtUtil.generateToken(userDetails);

        // generate refresh token
        UserEntity user = (UserEntity) userDetails;
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        List<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.name())
                .collect(Collectors.toList());

        return new AuthResponseDto(accessToken, refreshToken.getToken(), user.getUsername(), roles);
    }

    @Override
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto payload) {
        String token = payload.getRefreshToken();

        // find by token
        RefreshToken refreshToken = refreshTokenService.findByToken(token);

        try {
            refreshToken = refreshTokenService.verifyToken(refreshToken);
        } catch (AuthenticationException e) {
            throw new CustomAuthenticationException("Invalid refresh token");
        } catch (javax.naming.AuthenticationException e) {
            throw new RuntimeException(e);
        }

        // get user from refresh token
        UserEntity user = refreshToken.getUser();

        // generate new access token
        String newAccessToken = jwtUtil.generateToken(user);

        // rotate refresh token
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken);

        List<String> roleNames = user.getRoles().stream()
                .map(Enum::name) // "ADMIN"/"USER", no ROLE_ prefix, matches your Role enum
                .collect(Collectors.toList());

        return new RefreshTokenResponseDto(newAccessToken, newRefreshToken.getToken(), user.getUsername(),  roleNames, "Bearer");
    }
}
