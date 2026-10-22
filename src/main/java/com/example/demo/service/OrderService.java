package com.example.demo.service;

import com.example.demo.dto.request.OrderRequestDto;

public interface OrderService {
    void placeOrder(OrderRequestDto payload);
}
