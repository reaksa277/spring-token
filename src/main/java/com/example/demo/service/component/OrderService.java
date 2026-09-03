package com.example.demo.service.component;

import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final PaymentService paymentService;

    // inject dependency using constructor injection
    public void processOrder() {
        paymentService.pay();
    }
}
