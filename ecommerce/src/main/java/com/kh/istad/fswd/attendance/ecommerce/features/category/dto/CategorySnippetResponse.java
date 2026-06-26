package com.kh.istad.fswd.attendance.ecommerce.features.category.dto;

import lombok.Builder;

@Builder
public record CategorySnippetResponse(
        Integer id,
        String name
) {
}
