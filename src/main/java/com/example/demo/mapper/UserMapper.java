package com.example.demo.mapper;

import com.example.demo.common.enums.Role;
import com.example.demo.dto.request.AuthRequestDto;
import com.example.demo.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(AuthRequestDto dto) {
        UserEntity entity = new UserEntity();

        entity.setPassword(dto.getPassword());
        entity.setUsername(dto.getUsername());
        entity.setRole(Role.USER);

        return entity;
    }
}
