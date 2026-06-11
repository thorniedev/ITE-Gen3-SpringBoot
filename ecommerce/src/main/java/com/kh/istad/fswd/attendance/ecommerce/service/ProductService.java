package com.kh.istad.fswd.attendance.ecommerce.service;

import com.kh.istad.fswd.attendance.common.dto.PageResponse;
import com.kh.istad.fswd.attendance.ecommerce.dto.product.CreateProductRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.product.ProductFilterRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.product.ProductResponse;
import com.kh.istad.fswd.attendance.ecommerce.dto.seach.ProductAdvancedSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService
{
      // Implementation with Specification
      Page<ProductResponse> getProducts(
              Pageable pageable,
              ProductFilterRequest filter
      );

      // Advanced
      Page<ProductResponse> advancedSearch(
              ProductAdvancedSearchRequest request,
              Pageable pageable
      );

      // create as List
      List<ProductResponse> createProducts(
              List<CreateProductRequest> requests
      );

      ProductResponse create(CreateProductRequest createProductRequest);

      PageResponse<ProductResponse> findAllProducts(Integer pageNumber, Integer pageSize);

      ProductResponse findProductById(String id);

      ProductResponse findProductByName(String name);

      ProductResponse updateProductById(Integer id, CreateProductRequest createProductRequest);
}
