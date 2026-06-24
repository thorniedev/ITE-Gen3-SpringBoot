package com.kh.istad.fswd.attendance.ecommerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductRequest
        (
        @NotBlank
        String code,
        @NotBlank String name,
        @NotBlank String slug,
        String thumbnail,
        @NotNull
        @Positive
        BigDecimal unitPrice,
        @NotNull @PositiveOrZero
        Integer qty,
        String description,
        Boolean isAvailable,
        @NotNull Integer categoryId

        ){ }