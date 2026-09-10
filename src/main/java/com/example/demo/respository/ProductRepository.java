package com.example.demo.respository;

import com.example.demo.dto.response.ProductResponseDto;
import com.example.demo.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
//    List<ProductResponseDto> findByProductNameContainingIgnoreCase(String keyword);

    Boolean existsByProductName(String productName);

    Optional<ProductEntity> findByProductCode(String productCode);
}
