package com.example.demo.service;

import com.example.demo.dto.request.ProductRequestDto;
import com.example.demo.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> listProduct();

    void saveProduct(ProductRequestDto productRequestDto);

    ProductResponseDto updateProduct(String productCode, ProductRequestDto requestDto);

    void deleteProductByProductCode(String code);
}
