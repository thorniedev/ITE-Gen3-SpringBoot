package com.kh.istad.fswd.attendance.ecommerce.service;

import com.kh.istad.fswd.attendance.common.dto.PageResponse;
import com.kh.istad.fswd.attendance.ecommerce.dto.category.CreateCategoryRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.category.CreateCategoryResponse;
import org.springframework.data.domain.Page;

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
