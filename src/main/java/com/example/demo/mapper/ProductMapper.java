package com.example.demo.mapper;

import com.example.demo.dto.request.ProductRequestDto;
import com.example.demo.dto.response.ProductResponseDto;
import com.example.demo.model.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    public ProductEntity toEntity(ProductRequestDto dto) {
        ProductEntity entity = new ProductEntity();

        entity.setProductName(dto.getProductName());
        entity.setPrice(dto.getPrice());
        entity.setQuantity(dto.getQuantity());
        entity.setCategory(dto.getCategory());
        entity.setStatus(dto.getStatus());

        return entity;
    }

    public ProductResponseDto toDto(ProductEntity entity) {
        ProductResponseDto dto = new ProductResponseDto();

        dto.setProductCode(entity.getProductCode());
        dto.setProductName(entity.getProductName());
        dto.setPrice(entity.getPrice());
        dto.setCategory(entity.getCategory());
        dto.setQuantity(entity.getQuantity());
        dto.setStatus(entity.getStatus());
        dto.setCreatedOn(entity.getCreatedOn());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedOn(entity.getModifiedOn());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setIsDeleted(entity.getIsDeleted());

        return dto;
    }

    public List<ProductResponseDto> toDtoList(List<ProductEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void updateEntityFromDto(ProductRequestDto requestDto, ProductEntity existingEntity) {
        existingEntity.setProductName(requestDto.getProductName());
        existingEntity.setPrice(requestDto.getPrice());
        existingEntity.setQuantity(requestDto.getQuantity());
        existingEntity.setCategory(requestDto.getCategory());
        existingEntity.setStatus(requestDto.getStatus());
    }
}
