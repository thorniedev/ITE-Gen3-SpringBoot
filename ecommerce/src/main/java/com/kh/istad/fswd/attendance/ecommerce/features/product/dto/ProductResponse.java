package com.kh.istad.fswd.attendance.ecommerce.features.product.dto;

import com.kh.istad.fswd.attendance.ecommerce.features.category.dto.CreateCategoryResponse;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,
        String code,
        String name,
        String slug,
        String thumbnail,
        BigDecimal unitPrice,
        Integer qty,
        String description,
        Boolean isAvailable,
        Boolean isDeleted,
        CreateCategoryResponse category
) {}