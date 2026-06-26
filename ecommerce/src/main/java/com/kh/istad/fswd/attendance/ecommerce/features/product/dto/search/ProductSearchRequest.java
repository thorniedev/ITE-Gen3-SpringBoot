package com.kh.istad.fswd.attendance.ecommerce.features.product.dto.search;

import java.util.List;

public record ProductSearchRequest(
        List<SearchRequest> filters,
        GlobalOperator globalOperator
) {
    public enum GlobalOperator {
        AND,
        OR
    }
}