package com.example.demo.service.Impl;

import com.example.demo.dto.request.ProductRequestDto;
import com.example.demo.dto.response.ProductResponseDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.entity.ProductEntity;
import com.example.demo.respository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    private static final String MOCK_DATA_PATH = "mock-data/product-data.json";

    private final ObjectMapper mapper;

    @Override
    public List<ProductResponseDto> listProduct() {
        List<ProductEntity> productEntities = productRepository.findAll();

        return productMapper.toDtoList(productEntities);
    }

    @Override
    public void saveProduct(ProductRequestDto requestDto) {

        log.debug("Save request: {}", requestDto);
        if(productRepository.existsByProductName(requestDto.getProductName())) {
            throw new RuntimeException("product is already existed");
        }

        ProductEntity productEntity = productMapper.toEntity(requestDto);

        String productCode =
                "PRD-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        productEntity.setProductCode(productCode);

        ProductEntity save = productRepository.save(productEntity);

        productMapper.toDto(save);
    }
}
