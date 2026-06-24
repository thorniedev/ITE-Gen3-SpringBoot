package com.kh.istad.fswd.attendance.ecommerce.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record CreateCategoryResponse(

        Integer id,
        String name,
        String description,
        String icon,
        Boolean isDeleted,
        CreateCategoryResponse parentCategory
) {
}
