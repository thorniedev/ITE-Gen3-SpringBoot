package com.kh.istad.fswd.attendance.ecommerce.dto.product;

import com.kh.istad.fswd.attendance.ecommerce.dto.category.CreateCategoryResponse;

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