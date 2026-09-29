package com.example.demo.dto.protection;

import java.time.LocalDateTime;

public interface ProductProjection {
    int getId();

    String getProductCode();

    String getProductName();

    String getCategory();

    Float getPrice();

    Integer getQuantity();

    String getStatus();

    Boolean getIsDeleted();

    String getCreatedBy();

    LocalDateTime getCreatedOn();

    String getModifiedBy();

    LocalDateTime getModifiedOn();
}
