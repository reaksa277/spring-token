package com.example.demo.service;

import com.example.demo.dto.request.ProductRequestDto;
import com.example.demo.dto.request.RequestPageDto;
import com.example.demo.dto.response.PaginateResponse;
import com.example.demo.dto.response.ProductResponseDto;
import com.example.demo.exception.BusinessException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    PaginateResponse<ProductResponseDto> listProduct(RequestPageDto requestDto) throws BusinessException;

    void saveProduct(ProductRequestDto productRequestDto);

    ProductResponseDto updateProduct(String productCode, ProductRequestDto requestDto);

    void deleteProductByProductCode(String code);
}
