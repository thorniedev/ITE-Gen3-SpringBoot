package com.kh.istad.fswd.attendance.ecommerce.features.category;

import com.kh.istad.fswd.attendance.common.dto.PageResponse;
import com.kh.istad.fswd.attendance.ecommerce.features.category.dto.CreateCategoryRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.category.dto.CreateCategoryResponse;

import java.util.List;


public interface CategoryService {

    CreateCategoryResponse createNewCategory(CreateCategoryRequest createCategoryRequest);

    //
    List<CreateCategoryResponse> createCategories(
            List<CreateCategoryRequest> requests
    );

    // Get All product by pagination:
    PageResponse<CreateCategoryResponse> getAllCategories(
            Integer pageNumber,
            Integer pageSize
    );

    CreateCategoryResponse getCategoryById(Integer id);

    // GetSub categories
    List<CreateCategoryResponse> getSubCategories(Integer id);

    // Delete Category By ID
    void deleteCategoryById(Integer parentCategoryId);


    // Update Category By ID
    CreateCategoryResponse updateCategoryByID(Integer id, CreateCategoryRequest createCategoryRequest);

    // Update patch Category
    CreateCategoryResponse patchCategory(Integer id, CreateCategoryRequest request);
}
