package com.kh.istad.fswd.attendance.ecommerce.features.category;

import com.kh.istad.fswd.attendance.ecommerce.features.category.dto.CreateCategoryRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.category.dto.CreateCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "parentCategory", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category mapCreateCategoryRequestToCategory(CreateCategoryRequest createCategoryRequest);

    CreateCategoryResponse mapCategoryToCreateCategoryResponse(Category category);

}
