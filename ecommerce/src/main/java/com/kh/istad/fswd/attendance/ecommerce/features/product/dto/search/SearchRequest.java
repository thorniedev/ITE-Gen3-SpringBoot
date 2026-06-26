package com.kh.istad.fswd.attendance.ecommerce.features.product.dto.search;

public record SearchRequest(
        String column,
        String value,
        SearchOperation operation,
        String joinTable
) {
}