package com.example.demo.exception.model.product;

import java.util.Collection;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Collection<String> codes) {
        super("Product code(s) not found: " + String.join(", ", codes));
    }
}
