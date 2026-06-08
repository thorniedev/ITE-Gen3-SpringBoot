package com.kh.istad.fswd.attendance.ecommerce.service.impl
;

import com.kh.istad.fswd.attendance.ecommerce.domain.Category;
import com.kh.istad.fswd.attendance.ecommerce.dto.category.CreateCategoryRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.category.CreateCategoryResponse;
import com.kh.istad.fswd.attendance.ecommerce.mapper.CategoryMapper;
import com.kh.istad.fswd.attendance.ecommerce.repository.CategoryRepository;
import com.kh.istad.fswd.attendance.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CreateCategoryResponse createNewCategory(CreateCategoryRequest createCategoryRequest){

        //        log.info("createNewCategory");
    //
    //
    //        Category parentCategory = null;
    //
    //        boolean isExisting = categoryRepository
    //                .existsByName(createCategoryRequest.name());
    //
    //        if (isExisting)
    //            throw  new ResponseStatusException(
    //                    HttpStatus.CONFLICT,
    //                    "Create Category already exists"
    //            );
    //
    //        if (createCategoryRequest.parentCategoryId() != null ) {
    //            categoryRepository.findById(createCategoryRequest.parentCategoryId())
    //                    .orElseThrow(() -> new ResponseStatusException(
    //                            HttpStatus.NOT_FOUND,
    //                            "Parent Category Not Found"
    //                    ));
    //        }
    //
    //        // Map
    //        Category category = new Category();
    //        category.setName(createCategoryRequest.name());
    //        category.setDescription(createCategoryRequest.description());
    //        category.setIcon(createCategoryRequest.icon());
    //        category.setIsDeleted(false);
    //        category.setParentCategory(parentCategory);
    //
    //        category = categoryRepository.save(category);
    //
    //        // Insert if primary key is null
    //        // Update if primary key has value
    //        category = categoryRepository.save(category);
    //
    //        CreateCategoryResponse parentResponse = null;
    //
    //        if (parentCategory != null) {
    //            parentResponse = CreateCategoryResponse.builder()
    //                    .id(parentCategory.getId())
    //                    .name(parentCategory.getName())
    //                    .description(parentCategory.getDescription())
    //
    //                    // system provide
    //                    .icon(parentCategory.getIcon())
    //                    .isDeleted(parentCategory.getIsDeleted())
    //                    .build();
    //        }
    //
    //        // map entity to response
    //
    //        // CreateCategoryResponse parenCategoryResponse = CreateCategoryResponse.builder();
    //
    //
    //        return CreateCategoryResponse.builder()
    //                .id(category.getId())
    //                .name(category.getName())
    //                .description(category.getDescription())
    //                .icon(category.getIcon())
    //                .isDeleted(category.getIsDeleted())
    //                .createCategoryResponse(parentResponse)
    //                .build();

        if (categoryRepository.existsByName(createCategoryRequest.name())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category already exists"
            );
        }

        Category parentCategory = null;

        if (createCategoryRequest.parentCategoryId() != null) {
            parentCategory = categoryRepository
                    .findById(createCategoryRequest.parentCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Parent Category Not Found"
                    ));
        }

        Category category =
                categoryMapper.mapCreateCategoryRequestToCategory(createCategoryRequest);

        // system generate
        category.setParentCategory(parentCategory);
        category.setIsDeleted(false);   // was never set -> persisted as null

        // Insert if primary key is null
        // Update if primary key has value
        category = categoryRepository.save(category);

        return categoryMapper.mapCategoryToCreateCategoryResponse(category);
    }

    @Override
    public Page<CreateCategoryResponse> getAllCategories(Integer pageNumber, Integer pageSize) {

        //
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return categoryRepository.findAll(pageable)
                .map(categoryMapper::mapCategoryToCreateCategoryResponse);
    }

    // Find By ID
    @Override
    public CreateCategoryResponse getCategoryById(Integer id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category Not Found"
                ));

        return categoryMapper.mapCategoryToCreateCategoryResponse(category);

    }

    @Override
    public List<CreateCategoryResponse> getSubCategories(Integer id) {

        // This prevents when Get Blank category (if get null = Not Found)
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category Not Found"
                ));

        // if Exist but not parent_category
        if (category.getParentCategory() != null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "This Category Not Parent Category!!!"
            );
        }

        return categoryRepository
                .findByParentCategoryId(id)
                .stream()
                .map(categoryMapper::mapCategoryToCreateCategoryResponse)
                .toList();
    }

    // can not delete parent have sub-categories
    @Override
    public void deleteCategoryById(Integer parentCategoryId) {
        Category category = categoryRepository
                .findById(parentCategoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category Not Found"
                ));
        boolean hasChildren = categoryRepository.existsByParentCategoryId(parentCategoryId);
        if (hasChildren) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category has children"
            );
        }
        categoryRepository.delete(category);
    }

    @Override
    public CreateCategoryResponse updateCategoryByID(Integer id, CreateCategoryRequest createCategoryRequest) {
       Category category = categoryRepository
               .findById(id)
               .orElseThrow(() -> new ResponseStatusException(
                       HttpStatus.NOT_FOUND,
                       "Category Not Found"
               ));
       // prevent self-parent
        if (createCategoryRequest.parentCategoryId() != null
                && createCategoryRequest.parentCategoryId().equals(id)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Parent Category Id is already in use"
                );
        }
        Category parentCategory = null;

        if  (createCategoryRequest.parentCategoryId() != null) {
            parentCategory = categoryRepository
                    .findById(createCategoryRequest.parentCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Parent Category Not Found"
                    ));
        }

        category.setName(createCategoryRequest.name());
        category.setParentCategory(parentCategory);
        category.setIcon(createCategoryRequest.icon());

        category = categoryRepository.save(category);

        return categoryMapper.mapCategoryToCreateCategoryResponse(category);
    }

    @Override
    public CreateCategoryResponse patchCategory(Integer id, CreateCategoryRequest request) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category Not Found"
                ));
        if (request.name() != null ) {
            category.setName(request.name());
        }
        if (request.description() != null ) {
            category.setDescription(request.description());
        }
        if (request.icon() != null) {
            category.setIcon(request.icon());
        }

        if (request.parentCategoryId() != null) {
            Category parentCategory = categoryRepository
                    .findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Parent Category Not Found"
                    ));
            category.setParentCategory(parentCategory);
        }

        category = categoryRepository.save(category);

        return categoryMapper.mapCategoryToCreateCategoryResponse(category);
    }


}
