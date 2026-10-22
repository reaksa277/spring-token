package com.example.demo.mapper;

import com.example.demo.dto.request.OrderItemRequestDto;
import com.example.demo.dto.response.OrderItemResponseDto;
import com.example.demo.model.entity.OrderItemEntity;
import com.example.demo.model.entity.ProductEntity;
import com.example.demo.respository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.module.ResolutionException;
import java.util.List;

@Component
public class OrderItemMapper {
    @Autowired
    private ProductRepository productRepository;

    public OrderItemEntity toEntity(OrderItemRequestDto dto) {
        OrderItemEntity entity = new OrderItemEntity();

        entity.setProductCode(dto.getProductCode());
        entity.setQuantity(dto.getAmount());

        return entity;
    }

    public OrderItemResponseDto toResponseDto(OrderItemEntity entity) {

        if (entity == null) {
            return null;
        }

        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setProductCode(entity.getProductCode());
        dto.setPurchaseAmount(entity.getQuantity());

        // query get product
        ProductEntity product = productRepository.findByProductCode(entity.getProductCode())
                .orElseThrow(() -> new ResolutionException("product not found with code : " + entity.getProductCode()));

        dto.setProductName(product.getProductName());
        dto.setUnitPrice(product.getPrice());

        return dto;
    }

    public List<OrderItemResponseDto> toResponseDtoList(List<OrderItemEntity> entities) {
        return entities
                .stream()
                .map(orderItem -> this.toResponseDto(orderItem))
                .toList();
    }
}
