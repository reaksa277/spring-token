package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@JsonPropertyOrder({"product_code", "product_name", "quantity", "price", "category", "status", "createdAt"})
@Data
//@Builder
public class ProductResponseDto {
    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("product_name")
    private String productName;

    private String category;
    private Float price;
    private Integer quantity;
    private String status;
    private LocalDateTime createdOn;
    private String createdBy;
    private LocalDateTime modifiedOn;
    private String modifiedBy;
}
