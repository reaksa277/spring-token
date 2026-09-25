package com.example.demo.dto.request;

import com.example.demo.common.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @NotNull(message = "username is required")
    @NotBlank(message = "username must not be blank")
    private String username;

    @NotNull(message = "password is required")
    @NotBlank(message = "password must not be blank")
    @Size(min = 8)
    private String password;

    @NotNull(message = "roles is required")
    @NotEmpty(message = "roles must not be empty")
    private List<Role> roles;

}
