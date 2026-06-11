package com.kh.istad.fswd.attendance.ecommerce.mapper;

import com.kh.istad.fswd.attendance.ecommerce.domain.Product;
import com.kh.istad.fswd.attendance.ecommerce.dto.category.CreateCategoryRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.product.CreateProductRequest;
import com.kh.istad.fswd.attendance.ecommerce.dto.product.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper
{

  Product mapCreateProductRequestToProduct(CreateProductRequest createProductRequest);

  ProductResponse mapProductToProductResponse(Product product);
}
