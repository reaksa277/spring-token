package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPageDto {
    @NotNull(message = "page must not be null")
    @Min(value = 1, message = "page must be at least 1")
    private Integer page;

    @NotNull(message = "size must not be null")
    @Min(value = 1, message = "size must be at least 1")
    private Integer size;
}
