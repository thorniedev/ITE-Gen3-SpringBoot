package com.kh.istad.fswd.attendance.ecommerce.features.product;

import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.CreateProductRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper
{

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "code", ignore = true)
  @Mapping(target = "slug", ignore = true)
  @Mapping(target = "isDeleted", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "orderLines", ignore = true)
  Product mapCreateProductRequestToProduct(CreateProductRequest createProductRequest);

  ProductResponse mapProductToProductResponse(Product product);
}
