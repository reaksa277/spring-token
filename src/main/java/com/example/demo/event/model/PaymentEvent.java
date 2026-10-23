package com.example.demo.event.model;

import lombok.Data;

@Data
public class PaymentEvent {
    private String paymentId;
    private Long orderId;
    private Boolean status;
}
