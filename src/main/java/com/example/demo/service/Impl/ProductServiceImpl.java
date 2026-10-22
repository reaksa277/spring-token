package com.example.demo.service.Impl;

//import static com.example.demo.common.util.PageRequestValidationUtils.validatePaginationRequest;
import com.example.demo.common.util.PageRequestValidationUtils;

import com.example.demo.dto.protection.ProductProjection;
import com.example.demo.dto.request.ProductRequestDto;
import com.example.demo.dto.request.RequestPageDto;
import com.example.demo.dto.response.PaginateResponse;
import com.example.demo.dto.response.ProductResponseDto;
import com.example.demo.event.model.ProductEvent;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.model.ResourceNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.entity.ProductEntity;
import com.example.demo.respository.ProductRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.kafka.ProducerService;
import com.example.demo.service.mail.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ProducerService<ProductEvent> producerService;

    @Override
    @CachePut(value = "products", key = "#requestDto.productName")
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
//    @Caching(
//            put = @CachePut(value = "products", key = "#requestDto.productName"),
//            evict = @CacheEvict(value = "products-paginated", allEntries = true)
//    )
    public void saveProduct(ProductRequestDto requestDto) {
        String threadName = Thread.currentThread().getName();

        log.debug("Save request: {}", requestDto);
        if(productRepository.existsByProductName(requestDto.getProductName())) {
            throw new RuntimeException("product name is already existed");
        }
        log.info("[SYNC-PRODUCT] Creating new product | Thread: {}", threadName);

        ProductEntity productEntity = productMapper.toEntity(requestDto);

        String productCode =
                "PRD-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        productEntity.setProductCode(productCode);

        productRepository.save(productEntity);

        producerService.sendMessage("product.event", new ProductEvent(productEntity.getId(), productEntity.getQuantity()));

        log.info("[SYNC-PRODUCT] Product created successfully with Product: {} | Thread: {}", productEntity.getId() , threadName);
        log.info("[SYNC-PRODUCT] Trigger send notification asynchronously for Product: {} | Thread: {}", productEntity.getId() , threadName);

        notificationService.sendProductConfirmNotification(productEntity.getId(), "Your product has been completed");

//        productMapper.toDto(save);

        log.info("[SYNC-ORDER-COMPLETED] Completed order and triggered send notification");
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
