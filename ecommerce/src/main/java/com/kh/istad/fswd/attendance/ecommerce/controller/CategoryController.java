package com.kh.istad.fswd.attendance.ecommerce.controller;

import com.kh.istad.fswd.attendance.ecommerce.domain.Category;
import com.kh.istad.fswd.attendance.ecommerce.dto.category.CreateCategoryRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.category.CreateCategoryResponse;
import com.kh.istad.fswd.attendance.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController
{
    private final CategoryService categoryService;

    @PostMapping
    public CreateCategoryResponse createCategory(
            @Valid @RequestBody CreateCategoryRequest request
    )
    {
        return categoryService.createNewCategory(request);
    }

    // GET All categories(pagination)
    @GetMapping()
    public Page<CreateCategoryResponse> getCategory(
          @RequestParam(defaultValue = "0") Integer pageNumber,
          @RequestParam(defaultValue = "25") Integer pageSize
    ){
        return categoryService.getAllCategories(
                pageNumber, pageSize
        );
    }

    // GET category by ID
    @GetMapping("/{id}")
    public CreateCategoryResponse getCategoryById(
            @PathVariable Integer id
    ){
        return categoryService.getCategoryById(id);
    }

    // GET sub-categories by parentCategory_id
    @GetMapping("/{id}/sub-categories")
    public List<CreateCategoryResponse> getSubCategories(
           @PathVariable Integer id
    ){
        return categoryService.getSubCategories(id);
    }

    // Delete Parent Category By ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @PathVariable Integer id
    ) {
        categoryService.deleteCategoryById(id);
    }

    // Update Both parent and sub
    @PutMapping("/{id}")
    public CreateCategoryResponse updateCategoryById(
            @Valid @RequestBody CreateCategoryRequest request,
            @PathVariable Integer id){

        return categoryService.updateCategoryByID(id, request);
    }

    // Update Category Partial
    @PatchMapping("/{id}")
    public CreateCategoryResponse patchCategoryById(
            @Valid @PathVariable Integer id,
            @RequestBody CreateCategoryRequest request

    ) {
        return categoryService.updateCategoryByID(id, request);
    }
}
