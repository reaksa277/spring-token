package com.example.demo.service.Impl;

//import static com.example.demo.common.util.PageRequestValidationUtils.validatePaginationRequest;
import com.example.demo.common.util.PageRequestValidationUtils;

import com.example.demo.dto.protection.ProductProjection;
import com.example.demo.dto.request.ProductRequestDto;
import com.example.demo.dto.request.RequestPageDto;
import com.example.demo.dto.response.PaginateResponse;
import com.example.demo.dto.response.ProductResponseDto;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.model.ResourceNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.entity.ProductEntity;
import com.example.demo.respository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    public PaginateResponse<ProductResponseDto> listProduct(RequestPageDto requestPageDto) throws BusinessException {

        PageRequestValidationUtils.validatePaginationRequest(requestPageDto);

        Pageable pageable = PageRequest.of(requestPageDto.getPage() - 1, requestPageDto.getSize());

        Page<ProductEntity> productEntityPage = productRepository.findByIsDeletedFalse(pageable);

        log.info("List Product page {} of {}, totalElements={}",
                productEntityPage.getNumber(), productEntityPage.getTotalPages(), productEntityPage.getTotalElements());

        Page<ProductResponseDto> productDtoPage = productEntityPage.map(productMapper::toDto);

        return new PaginateResponse<>(productDtoPage);
    }

    @Override
    public ProductProjection retrieveProductById(int id) throws BusinessException {
        ProductProjection productProjection = productRepository
                .findProductById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found id: " + id)
                );

        return productProjection;
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

    @Override
    public ProductResponseDto updateProduct(String productCode, ProductRequestDto requestDto) {
        ProductEntity existing = productRepository
                .findByProductCode(productCode)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        productMapper.updateEntityFromDto(requestDto, existing);

        ProductEntity saved = productRepository.save(existing);

        return productMapper.toDto(saved);
    }

    @Override
    public void deleteProductByProductCode(String code) {
        ProductEntity existingProductEntity = productRepository
                .findByProductCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));
        existingProductEntity.setIsDeleted(true);

        productRepository.save(existingProductEntity);
    }
}
