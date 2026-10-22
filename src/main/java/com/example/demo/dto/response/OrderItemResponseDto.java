package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderItemResponseDto {
    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("purchase_amount")
    private Integer purchaseAmount;

    @JsonProperty("unit_price")
    private Float unitPrice;
}
