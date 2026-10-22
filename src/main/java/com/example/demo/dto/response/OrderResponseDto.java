package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonPropertyOrder({"order_id", "status", "total", "items", "created_at", "updated_at"})
public class OrderResponseDto {
    @JsonProperty("order_id")
    private Long id;

    private String status;

    private Double total;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("items")
    private List<OrderItemResponseDto> items;
}
