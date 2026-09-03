package com.example.demo.service.Impl;

import com.example.demo.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class CreditCardPaymentServiceImpl implements PaymentService {

    // provide an implementation
    @Override
    public void pay() {
        System.out.println("Payment done using Credit Card");
    }
}
