package com.kh.istad.fswd.attendance.ecommerce.dto.seach;

import java.util.List;

public record ProductAdvancedSearchRequest(
        List<SearchRequest> filters,
        GlobalOperator globalOperator
) {
    public enum GlobalOperator {
        AND,
        OR
    }
}