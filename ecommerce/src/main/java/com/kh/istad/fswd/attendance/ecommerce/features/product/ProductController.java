package com.kh.istad.fswd.attendance.ecommerce.controller;

import com.kh.istad.fswd.attendance.ecommerce.dto.product.CreateProductRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.product.ProductFilterRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.product.ProductResponse;
import com.kh.istad.fswd.attendance.ecommerce.dto.seach.ProductSearchRequest;
import com.kh.istad.fswd.attendance.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public List<ProductResponse> createProducts(
            @Valid @RequestBody List<CreateProductRequest> requests
    ) {
        return productService.createProducts(requests);
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
