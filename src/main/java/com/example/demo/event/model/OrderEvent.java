package com.example.demo.event.model;

import lombok.Data;

@Data
public class OrderEvent {
    private String orderId;
    private int productId;
    private int quantity;
}
