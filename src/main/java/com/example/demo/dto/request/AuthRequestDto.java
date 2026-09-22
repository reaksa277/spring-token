package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {

    @NotNull(message = "username is required")
    @NotBlank(message = "username must not be blank")
    private String username;

    @NotNull(message = "password is required")
    @NotBlank(message = "password must not be blank")
    @Size(min = 8)
    private String password;
}
