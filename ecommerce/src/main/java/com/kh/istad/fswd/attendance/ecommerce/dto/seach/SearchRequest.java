package com.kh.istad.fswd.attendance.ecommerce.dto.seach;

public record SearchRequest(
        String column,
        String value,
        SearchOperation operation,
        String joinTable
) {
}