package com.kh.istad.fswd.attendance.ecommerce.specification;

import com.kh.istad.fswd.attendance.ecommerce.features.product.Product;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.ProductFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> filter(ProductFilterRequest filter) {
        return Specification
                .where(keywordSearch(filter.keyword()))
                .and(codeContains(filter.code()))
                .and(nameContains(filter.name()))
                .and(slugContains(filter.slug()))
                .and(priceBetween(filter.minPrice(), filter.maxPrice()))
                .and(isAvailableEquals(filter.isAvailable()))
                .and(isDeletedEquals(filter.isDeleted()))
                .and(categoryIdEquals(filter.categoryId()));
    }

    private static Specification<Product> codeContains(String code) {
        return (root, query, cb) -> {
            if (code == null || code.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("code")),
                    "%" + code.toLowerCase() + "%"
            );
        };
    }

    private static Specification<Product> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    private static Specification<Product> slugContains(String slug) {
        return (root, query, cb) -> {
            if (slug == null || slug.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("slug")),
                    "%" + slug.toLowerCase() + "%"
            );
        };
    }

    private static Specification<Product> priceBetween(
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        return (root, query, cb) -> {

            if (minPrice != null && maxPrice != null) {
                return cb.between(
                        root.get("unitPrice"),
                        minPrice,
                        maxPrice
                );
            }

            if (minPrice != null) {
                return cb.greaterThanOrEqualTo(
                        root.get("unitPrice"),
                        minPrice
                );
            }

            if (maxPrice != null) {
                return cb.lessThanOrEqualTo(
                        root.get("unitPrice"),
                        maxPrice
                );
            }

            return cb.conjunction();
        };
    }

    private static Specification<Product> isAvailableEquals(Boolean isAvailable) {
        return (root, query, cb) -> {
            if (isAvailable == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("isAvailable"),
                    isAvailable
            );
        };
    }

    private static Specification<Product> isDeletedEquals(Boolean isDeleted) {
        return (root, query, cb) -> {
            if (isDeleted == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("isDeleted"),
                    isDeleted
            );
        };
    }

    private static Specification<Product> categoryIdEquals(Integer categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("category").get("id"),
                    categoryId
            );
        };
    }

   // keyword
    private static Specification<Product> keywordSearch(
            String keyword
    ) {
        return (root, query, cb) -> {

            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }

            String value =
                    "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("code")), value),
                    cb.like(cb.lower(root.get("name")), value),
                    cb.like(cb.lower(root.get("slug")), value)
            );
        };
    }
}





//package com.kh.istad.fswd.attendance.ecommerce.specification;
//
//import com.kh.istad.fswd.attendance.ecommerce.features.product.Product;
//import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.ProductFilterRequest;
//import org.springframework.data.jpa.entity.Specification;
//
//import java.math.BigDecimal;
//
//public class ProductSpecification
//{
//    public static Specification<Product> filter(ProductFilterRequest filter)
//    {
//        return Specification
//                .where(codeContains(filter.code()))
//                .and(nameContains(filter.name()))
//                .and(slugContains(filter.slug()))
//                .and(priceGreaterThanOrEqual(filter.minPrice()))
//                .and(priceLessThanOrEqual(filter.maxPrice()))
//                .and(isAvailableEquals(filter.isAvailable()))
//                .and(isDeletedEquals(filter.isDeleted()))
//                .and(categoryIdEquals(filter.categoryId()))
//                .and(priceBetween(filter.minPrice(), filter.maxPrice()));
//    }
//
//    public static Specification<Product> codeContains(String code)
//    {
//        return (root, query, cb) ->{
//            if (code == null || code.isBlank()) {
//                return cb.conjunction();
//            }
//
//            return cb.like(
//                cb.lower(root.get("code")),
//                    "%" + code.toLowerCase() + "%"
//            );
//        };
//    }
//
//    public static Specification <Product> nameContains(String name){
//        return (root, query, cb) -> {
//                if (name == null || name.isBlank()) {
//                    return cb.conjunction();
//                }
//
//                return cb.like(
//                        cb.lower(root.get("code")),
//                        "%" + name.toLowerCase() + "%"
//                );
//        };
//    }
//
//    // for slug
//    public static Specification <Product> slugContains(String slug) {
//        return (root, qury, cb) -> {
//            if (slug == null || slug.isBlank()) {
//                return cb.conjunction();
//            }
//
//            return cb.like(
//                    cb.lower(root.get("slug")),
//                    "%" + slug.toLowerCase() + "%"
//            );
//        };
//    }
//
//    // For Price
//    public static Specification <Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
//        return (root, query, cb) -> {
//           if (minPrice == null) {
//               return cb.conjunction();
//           }
//
//           return  cb.greaterThanOrEqualTo(root.get("price"), minPrice);
//        };
//    }
//
//    public static Specification <Product> priceLessThanOrEqual(BigDecimal maxPrice) {
//        return (root, query, cb) -> {
//            if (maxPrice == null) {
//                return cb.conjunction();
//            }
//
//            return  cb.lessThanOrEqualTo(root.get("price"), maxPrice);
//        };
//    }
//
//    public static Specification <Product> isAvailableEquals(Boolean isAvailable) {
//        return (root, query, cb) -> {
//            if (isAvailable == null) {
//                return cb.conjunction();
//            }
//
//            return cb.equal(root.get("isAvailable"), isAvailable);
//        };
//    }
//
//    public static Specification <Product> isDeletedEquals(Boolean isDeleted) {
//        return (root, query, cb) ->{
//            if (isDeleted == null) {
//                return cb.conjunction();
//            }
//
//            return cb.equal(root.get("isDeleted"), isDeleted);
//        };
//    }
//
//    //
//    public static Specification <Product> categoryIdEquals(Integer categoryId) {
//        return (root, query, cb) -> {
//            if (categoryId == null) {
//                return cb.conjunction();
//            }
//
//            return cb.equal(root.get("categoryId"), categoryId);
//        };
//    }
//
//    // priceBetween
//    public static Specification <Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
//        return (root, query, cb) -> {
//
//            if (minPrice == null || maxPrice == null) {
//                return cb.between(root.get("unitPrice"), minPrice, maxPrice);
//            }
//
//            if (minPrice == null) {
//                return cb.greaterThanOrEqualTo(root.get("unitPrice"), minPrice);
//            }
//
//            if (maxPrice == null) {
//                return cb.lessThanOrEqualTo(root.get("unitPrice"), maxPrice);
//            }
//            return cb.conjunction();
//        };
//    }
//}
