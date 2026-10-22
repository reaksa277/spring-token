package com.example.demo.dto.request;

import com.example.demo.common.annotations.ValidEnum;
import com.example.demo.common.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderUpdateRequestDto {
    @JsonProperty("status")
    @ValidEnum(enumClass = OrderStatus.class, message = "Value must be one of PENDING,FAILED,SUCCESS")
    private String status;
}
