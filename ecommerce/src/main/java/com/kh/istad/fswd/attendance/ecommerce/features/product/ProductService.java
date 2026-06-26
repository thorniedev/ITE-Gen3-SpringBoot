package com.kh.istad.fswd.attendance.ecommerce.features.product;

import com.kh.istad.fswd.attendance.common.dto.PageResponse;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.CreateProductRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.ProductFilterRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.ProductResponse;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.search.ProductSearchRequest;
import org.springframework.data.domain.Page;
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
              ProductSearchRequest request,
              Pageable pageable
      );

      List<ProductResponse> createProducts(
              List<CreateProductRequest> requests
      );

      /**
       * Create a new product
       * @param createProductRequest is requesting data for creating product
       * @return {@link ProductResponse}
       * @author kim chanthorn
       * @since June 23, 2026
       */
      ProductResponse create(CreateProductRequest createProductRequest);

      /**
       * Find products by pagination.
       *
       * @param pageNumber the page index to retrieve (zero-based or one-based depending on framework)
       * @param pageSize   the number of products per page to retrieve
       * @return a {@link PageResponse} containing a paginated list of {@link ProductResponse}
       * @author Kim Chanthorn
       * @since June 23, 2026
       */
      Page<ProductResponse> findAllProducts(Integer pageNumber, Integer pageSize);
      //PageResponse<ProductResponse> findAllProducts(Integer pageNumber, Integer pageSize);

      ProductResponse findProductById(String id);

      ProductResponse findProductByName(String name);

      ProductResponse updateProductById(Integer id, CreateProductRequest createProductRequest);
}
