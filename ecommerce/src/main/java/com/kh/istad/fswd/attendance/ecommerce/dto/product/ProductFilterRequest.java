package com.kh.istad.fswd.attendance.ecommerce.dto.product;

import java.math.BigDecimal;

public record ProductFilterRequest(
        String keyword,
        String code,
        String name,
        String slug,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Boolean isAvailable,
        Boolean isDeleted,
        Integer categoryId
) {
}