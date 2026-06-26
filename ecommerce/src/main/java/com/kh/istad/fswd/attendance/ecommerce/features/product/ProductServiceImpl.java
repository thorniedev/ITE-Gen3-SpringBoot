package com.kh.istad.fswd.attendance.ecommerce.features.product
;

import com.kh.istad.fswd.attendance.common.dto.PageResponse;
import com.kh.istad.fswd.attendance.common.exception.ConflictException;
import com.kh.istad.fswd.attendance.common.exception.ResourceNotFoundException;
import com.kh.istad.fswd.attendance.common.util.ProductDataUtil;
import com.kh.istad.fswd.attendance.common.util.SlugUtil;
import com.kh.istad.fswd.attendance.ecommerce.features.category.Category;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.CreateProductRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.ProductFilterRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.ProductResponse;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.search.ProductSearchRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.category.CategoryRepository;
import com.kh.istad.fswd.attendance.ecommerce.specification.ProductAdvancedSpecification;
import com.kh.istad.fswd.attendance.ecommerce.specification.ProductSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService
{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    // Get Product with Specification
    public Page<ProductResponse> getProducts(Pageable pageable,
                                             ProductFilterRequest filter
    ) {
        Specification<Product> spec =
                ProductSpecification.filter(filter);

        return productRepository.findAll(spec, pageable)
                .map(productMapper::mapProductToProductResponse);

    }

    @Override
    public Page<ProductResponse> advancedSearch(
            ProductSearchRequest request,
            Pageable pageable
    ) {
        Specification<Product>
                spec = ProductAdvancedSpecification.filter(request);

        return productRepository
                .findAll(spec, pageable)
                .map(productMapper::mapProductToProductResponse);
    }

    @Override
    public ProductResponse create(CreateProductRequest createProductRequest) {

        if (productRepository.existsByName(createProductRequest.name())) {
            throw new ConflictException("Product has already in used!");
        }

        Category category = categoryRepository
                .findByIdAndIsDeletedFalse(createProductRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found"
                ));

        // Transfer data from DTO to Model
        Product product = productMapper
                .mapCreateProductRequestToProduct(createProductRequest);

        // ====> Set generated system data <=========

        // product.setCode(generateProductCode());
        // product.setSlug(generateUniqueSlug(createProductRequest.name()));

        // New version generated
        product.setCode(ProductDataUtil.generateUniqueCode(productRepository::existsByCode));
        product.setSlug(ProductDataUtil.generateUniqueSlug(createProductRequest.name(), productRepository::existsBySlug));

        product.setCategory(category); // -> done
        product.setIsDeleted(false); // -> done
        product.setIsAvailable(createProductRequest.isAvailable() != null ? createProductRequest.isAvailable() : true);

        product = productRepository.save(product);

        return productMapper.mapProductToProductResponse(product);
    }

    @Override
    public List<ProductResponse> createProducts(
            List<CreateProductRequest> requests
    ) {
        return requests.stream()
                .map(this::create)
                .toList();
    }

    @Override
    public Page<ProductResponse> findAllProducts(Integer pageNumber, Integer pageSize) {

        Sort sortById = Sort.by(Sort.Direction.DESC, "id");

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortById);

        Page<Product> products = productRepository.findAll(pageable);

        return products.map(productMapper::mapProductToProductResponse);

//                Page<ProductResponse> page =
//                productRepository.findAll(pageable)
//                        .map(productMapper::mapProductToProductResponse);

//        return PageResponse.<ProductResponse>builder()
//                .contents(page.getContent())
//                .pageNumber(page.getNumber())
//                .pageSize(page.getSize())
//                .totalElements(page.getTotalElements())
//                .totalPages(page.getTotalPages())
//                .first(page.isFirst())
//                .last(page.isLast())
//                .build();

        //        Page<Product> products = productRepository.findAll(pageable);
        //
        //        return products

    }

    @Override
    public ProductResponse findProductById(String id) {
        return null;
    }

    @Override
    public ProductResponse findProductByName(String name) {
        return null;
    }

    @Override
    public ProductResponse updateProductById(Integer id, CreateProductRequest createProductRequest) {
        return null;
    }


    // Utils (Not yet used)
    private String generateProductCode() {
        String code;

        do {
            code = "ITE-3RD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (productRepository.existsByCode(code));

        return code;
    }

    private String generateUniqueSlug(String name) {
        String baseSlug = SlugUtil.generateSlug(name);
        String slug = baseSlug;
        int suffix = 1;

        while (productRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + suffix;
            suffix++;
        }
        return slug;
    }

}
