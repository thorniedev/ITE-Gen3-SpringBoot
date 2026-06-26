package com.kh.istad.fswd.attendance.ecommerce.features.category
;

import com.kh.istad.fswd.attendance.common.dto.PageResponse;
import com.kh.istad.fswd.attendance.common.exception.ConflictException;
import com.kh.istad.fswd.attendance.common.exception.ResourceNotFoundException;
import com.kh.istad.fswd.attendance.common.util.CategoryDataUtil;
import com.kh.istad.fswd.attendance.ecommerce.features.category.dto.CreateCategoryRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.category.dto.CreateCategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
            throw new ConflictException(
                    // HttpStatus.CONFLICT,
                    "Category already exists"
            );
        }

        Category parentCategory = null;

        if (createCategoryRequest.parentCategoryId() != null) {
            parentCategory = categoryRepository
                    .findById(createCategoryRequest.parentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            //HttpStatus.NOT_FOUND,
                            "Parent Category Not Found"
                    ));
        }

        Category category =
                categoryMapper.mapCreateCategoryRequestToCategory(createCategoryRequest);

        // system generated data
        category.setParentCategory(parentCategory);

        // Generate code
        // category.setCode(generateCategoryCode());
        category.setCode(CategoryDataUtil.generateUniqueCode(categoryRepository::existsByCode));
        category.setIsDeleted(false);

        // Insert if primary key is null
        // Update if primary key has value
        category = categoryRepository.save(category);

        return categoryMapper.mapCategoryToCreateCategoryResponse(category);
    }

    @Override
    public List<CreateCategoryResponse> createCategories(
            List<CreateCategoryRequest> requests
    ) {

        List<Category> categories = requests.stream()
                .map(categoryMapper::mapCreateCategoryRequestToCategory)
                .peek(category -> {
                    //category.setCode(generateCategoryCode());
                    category.setCode(CategoryDataUtil.generateUniqueCode(categoryRepository::existsByCode));
                    category.setIsDeleted(false);
                })
                .toList();

        List<Category> savedCategories =
                categoryRepository.saveAll(categories);

        return savedCategories.stream()
                .map(categoryMapper::mapCategoryToCreateCategoryResponse)
                .toList();
    }

    @Override
    public PageResponse<CreateCategoryResponse> getAllCategories(Integer pageNumber, Integer pageSize) {

        //
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        /*
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::mapCategoryToCreateCategoryResponse); */

        // with PageResponse for common module

        Page<CreateCategoryResponse> page =
                categoryRepository.findAll(pageable)
                        .map(categoryMapper::mapCategoryToCreateCategoryResponse);
        return PageResponse.<CreateCategoryResponse>builder()
                .contents(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    // Find By ID
    @Override
    public CreateCategoryResponse getCategoryById(Integer id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        // HttpStatus.NOT_FOUND,
                        "Category has not found!"
                ));

        return categoryMapper.mapCategoryToCreateCategoryResponse(category);

    }

    @Override
    public List<CreateCategoryResponse> getSubCategories(Integer id) {

        // This prevents when Get Blank category (if get null = Not Found)
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        // HttpStatus.NOT_FOUND,
                        "Category Not Found"
                ));

        // if Exist but not parent_category
        if (category.getParentCategory() != null) {
            throw new ResourceNotFoundException(
                    // HttpStatus.NOT_FOUND,
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
                .orElseThrow(() -> new ResourceNotFoundException(
                        // HttpStatus.NOT_FOUND,
                        "Category Not Found"
                ));
        boolean hasChildren = categoryRepository.existsByParentCategoryId(parentCategoryId);
        if (hasChildren) {
            throw new ConflictException(
                    // HttpStatus.CONFLICT,
                    "Category has children"
            );
        }
        categoryRepository.delete(category);
    }

    @Override
    public CreateCategoryResponse updateCategoryByID(Integer id, CreateCategoryRequest createCategoryRequest) {
       Category category = categoryRepository
               .findById(id)
               .orElseThrow(() -> new ResourceNotFoundException(
                       // HttpStatus.NOT_FOUND,
                       "Category Not Found"
               ));
        // prevent self-parent
        if (createCategoryRequest.parentCategoryId() != null
                && createCategoryRequest.parentCategoryId().equals(id)) {
                throw new ConflictException(
                        // HttpStatus.CONFLICT,
                        "Parent Category Id is already in use"
                );
        }
        Category parentCategory = null;

        if  (createCategoryRequest.parentCategoryId() != null) {
            parentCategory = categoryRepository
                    .findById(createCategoryRequest.parentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            // HttpStatus.NOT_FOUND,
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
                .orElseThrow(() -> new ResourceNotFoundException(
                        // HttpStatus.NOT_FOUND,
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
            if (request.parentCategoryId().equals(id)) {
                throw new ConflictException("Category cannot be parent of itself");
            }

            Category parentCategory = categoryRepository
                    .findById(request.parentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Parent Category Not Found"
                    ));

            category.setParentCategory(parentCategory);
        }

        category = categoryRepository.save(category);

        return categoryMapper.mapCategoryToCreateCategoryResponse(category);
    }

    /**
     * Generates a unique category code following the format ITE-CAT-b3f91a2-*******
     *
     * @return the generated category code string
     * @author Kim Chanthorn
     * @since June 23, 2026
     */
    private String generateCategoryCode() {
        String code;

        do {
            String cleanUuid = UUID.randomUUID().toString().replaceAll("-", "");
            String uniqueSuffix = cleanUuid.substring(0, 7);
            code = "ITE-CAT-" + uniqueSuffix;
        } while (categoryRepository.existsByCode(code));

        return code;

    }

}
