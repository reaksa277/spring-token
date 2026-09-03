package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonPropertyOrder({"id", "productCode", "productName", "category", "status", "createdAt"})
@Getter
@Setter
public class ProductResponseDto {

    private Long id;
    private String productCode;
    private String productName;
    private String category;
    private String status;
    private LocalDateTime createdAt;
}
