package com.kh.istad.fswd.attendance.ecommerce.mapper;

import com.kh.istad.fswd.attendance.ecommerce.domain.Category;
import com.kh.istad.fswd.attendance.ecommerce.dto.category.CreateCategoryRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.category.CreateCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category mapCreateCategoryRequestToCategory(CreateCategoryRequest createCategoryRequest);

    CreateCategoryResponse mapCategoryToCreateCategoryResponse(Category category);


    // Map to Category return type target want map and parameter is a source
    // Return Type = Target
    // Parameters = Source
    //    public Category mapCreateCategoryRequestToCategory(
    //            CreateCategoryRequest request
    //    ) {


    //        Category category = new Category();
    //
    //        category.setName(request.name());
    //        category.setDescription(request.description());
    //        category.setIcon(request.icon());
    //
    //        return category;
    // }

}
