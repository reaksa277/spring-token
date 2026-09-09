package com.example.demo.dto.request;

import com.example.demo.common.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
//    private String productCode;

    @NotBlank(message = "Product Name can not be blank")
    @Size(max = 250, message = "Account Name must not exceed 250 characters")
    private String productName;

    @NotNull(message = "Category can not be null")
    private String category;

    @NotNull(message = "Price can not be null")
    @Positive(message = "Price must be positive")
    private Float price;

    @NotNull(message = "Quantity can not be null")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    private String status = Status.ACTIVE.name();
}
