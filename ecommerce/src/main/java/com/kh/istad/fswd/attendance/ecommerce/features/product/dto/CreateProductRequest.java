package com.kh.istad.fswd.attendance.ecommerce.features.product.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest
        (

        @NotBlank(message = "Name is required")
        @Size(max = 255)
        String name,

        String thumbnail,
        
        @NotNull(message = "UnitPrice is required")
        @Positive
        BigDecimal unitPrice,

        @NotNull(message = "Quantity is required")
        @PositiveOrZero
        Integer qty,

        @Size(max = 500)
        String description,

        Boolean isAvailable,

        @NotNull(message = "categoryId is required")
        @Positive
        Integer categoryId
        ){ }
