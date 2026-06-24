package com.kh.istad.fswd.attendance.ecommerce.mapper;

import com.kh.istad.fswd.attendance.ecommerce.features.category.Category;
import com.kh.istad.fswd.attendance.ecommerce.features.category.dto.CreateCategoryRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.category.dto.CreateCategoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category mapCreateCategoryRequestToCategory(CreateCategoryRequest createCategoryRequest);

    CreateCategoryResponse mapCategoryToCreateCategoryResponse(Category category);

}
