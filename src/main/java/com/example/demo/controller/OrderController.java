package com.example.demo.controller;

import com.example.demo.dto.Response;
import com.example.demo.dto.request.OrderRequestDto;
import com.example.demo.dto.request.OrderUpdateRequestDto;
import com.example.demo.dto.response.OrderResponseDto;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;

//    @PostMapping("/process-order")
//    public String processOrder() {
//
//        orderService.processOrder();
//        return "Order processed";
//    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Response<OrderResponseDto>> placeOrder(@Valid @RequestBody OrderRequestDto payload) {
        orderService.placeOrder(payload);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("201","success","successfully created order"));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Response<List<OrderResponseDto>>> listOrder() {
        List<OrderResponseDto> orders = orderService.listOrders();

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success","successfully retrieved orders",orders));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PatchMapping("order-status")
    public ResponseEntity<Response<OrderResponseDto>> updateOrderStatus(@RequestParam Long orderId, @Valid @RequestBody OrderUpdateRequestDto payload) {
        OrderResponseDto updateOrder = orderService.updateOrderStatus(orderId, payload);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success","successfully updated order", updateOrder));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Response<OrderResponseDto>> deleteOrder(@RequestParam Long orderId) {
        orderService.deleteOrder(orderId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("success","successfully deleted order: " + orderId));
    }
}