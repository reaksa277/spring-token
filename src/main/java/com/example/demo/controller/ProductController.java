package com.example.demo.controller;

import com.example.demo.dto.request.ProductRequestDto;
import com.example.demo.dto.request.RequestPageDto;
import com.example.demo.dto.response.PaginateResponse;
import com.example.demo.dto.response.ProductResponseDto;
import com.example.demo.dto.Response;
import com.example.demo.exception.BusinessException;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list-page")
    public ResponseEntity<Response<PaginateResponse<ProductResponseDto>>> listProduct(
           @Valid @RequestBody RequestPageDto requestDto) throws BusinessException {

        PaginateResponse<ProductResponseDto> products = productService.listProduct(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200", "success", "successfully retrieved product", products));
    }

    @PostMapping
    public ResponseEntity<Response<ProductResponseDto>> saveProduct(@Valid @RequestBody ProductRequestDto payload) {
        productService.saveProduct(payload);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("201","success","successfully save product"));
    }

    @PutMapping("/update")
    public ResponseEntity<Response<ProductResponseDto>> updateProduct(
            @RequestParam String productCode,
            @Valid @RequestBody ProductRequestDto requestDto) {

        ProductResponseDto res =
                productService.updateProduct(productCode, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success", "successfully update product", res));
    }

    @DeleteMapping
    public ResponseEntity<Response<ProductResponseDto>> deleteProductByProductCode(@RequestParam String code) {
        productService.deleteProductByProductCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200", "success", "successfully delete product"));
    }
}
