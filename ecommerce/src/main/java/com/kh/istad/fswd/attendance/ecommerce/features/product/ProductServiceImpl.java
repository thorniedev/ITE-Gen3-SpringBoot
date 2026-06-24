package com.kh.istad.fswd.attendance.ecommerce.service.impl
;

import com.kh.istad.fswd.attendance.common.dto.PageResponse;
import com.kh.istad.fswd.attendance.common.exception.ConflictException;
import com.kh.istad.fswd.attendance.common.exception.ResourceNotFoundException;
import com.kh.istad.fswd.attendance.ecommerce.entity.Category;
import com.kh.istad.fswd.attendance.ecommerce.entity.Product;
import com.kh.istad.fswd.attendance.ecommerce.dto.product.CreateProductRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.product.ProductFilterRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.product.ProductResponse;
import com.kh.istad.fswd.attendance.ecommerce.dto.seach.ProductSearchRequest;
import com.kh.istad.fswd.attendance.ecommerce.mapper.ProductMapper;
import com.kh.istad.fswd.attendance.ecommerce.repository.CategoryRepository;
import com.kh.istad.fswd.attendance.ecommerce.repository.ProductRepository;
import com.kh.istad.fswd.attendance.ecommerce.service.ProductService;
import com.kh.istad.fswd.attendance.ecommerce.specification.ProductAdvancedSpecification;
import com.kh.istad.fswd.attendance.ecommerce.specification.ProductSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
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

        if (productRepository.existsByCode(createProductRequest.code())) {
            throw new ConflictException(
                    "Product category already exists"
            );
        }
        if (productRepository.existsBySlug(createProductRequest.slug())) {
            throw new ConflictException(
                    "Product category already exists"
            );
        }

        Category category = categoryRepository
                .findByIdAndIsDeletedFalse(createProductRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found"
                ));

        Product product = productMapper.mapCreateProductRequestToProduct(createProductRequest);

        product.setCategory(category);
        product.setIsDeleted(false);
        product.setIsAvailable(createProductRequest.isAvailable() != null ? createProductRequest.isAvailable() : true);
        productRepository.save(product);

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
    public PageResponse<ProductResponse> findAllProducts(Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<ProductResponse> page =
                productRepository.findAll(pageable)
                        .map(productMapper::mapProductToProductResponse);

        return PageResponse.<ProductResponse>builder()
                .contents(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
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
}
