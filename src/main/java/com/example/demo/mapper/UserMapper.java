package com.example.demo.mapper;

import com.example.demo.common.enums.Role;
import com.example.demo.dto.request.AuthRequestDto;
import com.example.demo.dto.request.RegisterRequestDto;
import com.example.demo.model.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserMapper {

    public UserEntity toEntity(RegisterRequestDto dto) {
        UserEntity entity = new UserEntity();

        entity.setPassword(dto.getPassword());
        entity.setUsername(dto.getUsername());
        entity.setRoles(new HashSet<>(dto.getRoles()));

        return entity;
    }
}
