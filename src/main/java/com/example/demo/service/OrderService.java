package com.example.demo.service;

import com.example.demo.dto.request.OrderRequestDto;
import com.example.demo.dto.request.OrderUpdateRequestDto;
import com.example.demo.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService {
    void placeOrder(OrderRequestDto payload);

    List<OrderResponseDto> listOrders();

    OrderResponseDto updateOrderStatus(Long orderId, OrderUpdateRequestDto payload);

    void deleteOrder(Long orderId);
}
