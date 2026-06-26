package com.kh.istad.fswd.attendance.ecommerce.features.product;

import com.kh.istad.fswd.attendance.common.dto.PageResponse;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.CreateProductRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.ProductFilterRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.ProductResponse;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.search.ProductSearchRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController
{
        private final ProductService productService;
        // private final ProductServiceImpl productServiceImpl;

    @GetMapping("/spec")
    public Page<ProductResponse> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String slug,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) Integer categoryId,
            Pageable pageable
    ) {
        ProductFilterRequest filter = new ProductFilterRequest(
                keyword,
                code,
                name,
                slug,
                minPrice,
                maxPrice,
                isAvailable,
                isDeleted,
                categoryId
        );

        return productService.getProducts(pageable, filter);
    }

    // Create product as List
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ProductResponse> createProducts(
            @Valid @RequestBody List<CreateProductRequest> requests
    ) {
        return productService.createProducts(requests);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        return productService.create(request);
    }

    @GetMapping()
    public Page<ProductResponse> getProducts(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "25") Integer pageSize
    ){
        return productService.findAllProducts(pageNumber, pageSize);
    }

    @PostMapping("/search")
    public Page<ProductResponse> searchProducts(
            @RequestBody ProductFilterRequest filter,
            Pageable pageable
    ) {
        return productService.getProducts(pageable, filter);
    }

    @PostMapping("/advanced-search")
    public Page<ProductResponse> advancedSearch(
            @RequestBody ProductSearchRequest request,
            Pageable pageable
    ) {
        return productService.advancedSearch(request, pageable);
    }
}
