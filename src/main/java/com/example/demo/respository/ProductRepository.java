package com.example.demo.respository;

import com.example.demo.dto.protection.ProductProjection;
import com.example.demo.model.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
//    List<ProductResponseDto> findByProductNameContainingIgnoreCase(String keyword);
    Page<ProductEntity> findByIsDeletedFalse(Pageable pageable);

    Boolean existsByProductName(String productName);

    Optional<ProductEntity> findByProductCode(String productCode);

    @Query(value = """
    SELECT
        p.id,
        p.product_code as productCode,
        p.product_name as product_name,
        p.price,
        p.quantity,
        p.category,
        p.status,
        p.created_on AS createdOn,
        p.created_by AS createdBy,
        p.modified_on AS modifiedOn,
        p.modified_by AS modifiedBy,
        p.is_delete AS isDeleted
        FROM products p
        WHERE p.id = :id
        AND p.is_delete = false
    """, nativeQuery = true)
    Optional<ProductProjection> findProductById(int id);
}
