package com.kh.istad.fswd.attendance.ecommerce.features.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(

        @NotBlank(message = "Name id require")
        String name,

        String description,
        String icon,
        Integer parentCategoryId
) {
}
