package com.kh.istad.fswd.attendance.ecommerce.specification;


import com.kh.istad.fswd.attendance.ecommerce.features.product.Product;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.search.ProductSearchRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.search.SearchRequest;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductAdvancedSpecification {

    private static final List<String> ALLOWED_COLUMNS = List.of(
            "code",
            "name",
            "slug",
            "unitPrice",
            "qty",
            "isAvailable",
            "isDeleted"
    );

    private static final List<String> ALLOWED_JOINS = List.of(
            "category"
    );

    public static Specification<Product> filter(
            ProductSearchRequest request
    ) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (request == null || request.filters() == null) {
                return criteriaBuilder.conjunction();
            }

            for (SearchRequest filter : request.filters()) {

                if (filter.column() == null || filter.operation() == null) {
                    continue;
                }

                switch (filter.operation()) {

                    case EQUAL -> {
                        validateColumn(filter.column());

                        predicates.add(
                                criteriaBuilder.equal(
                                        root.get(filter.column()),
                                        convertValue(filter.column(), filter.value())
                                )
                        );
                    }

                    case LIKE -> {
                        validateColumn(filter.column());

                        predicates.add(
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get(filter.column())),
                                        "%" + filter.value().toLowerCase() + "%"
                                )
                        );
                    }

                    case IN -> {
                        validateColumn(filter.column());

                        List<String> values = Arrays.stream(filter.value().split(","))
                                .map(String::trim)
                                .toList();

                        predicates.add(
                                root.get(filter.column()).in(values)
                        );
                    }

                    case GREATER_THAN -> {
                        validateColumn(filter.column());

                        predicates.add(
                                criteriaBuilder.greaterThan(
                                        root.get(filter.column()),
                                        filter.value()
                                )
                        );
                    }

                    case LESS_THAN -> {
                        validateColumn(filter.column());

                        predicates.add(
                                criteriaBuilder.lessThan(
                                        root.get(filter.column()),
                                        filter.value()
                                )
                        );
                    }

                    case BETWEEN -> {
                        validateColumn(filter.column());

                        String[] values = filter.value().split(",");

                        if (values.length != 2) {
                            throw new IllegalArgumentException(
                                    "BETWEEN value must be like: 100,500"
                            );
                        }

                        predicates.add(
                                criteriaBuilder.between(
                                        root.get(filter.column()),
                                        new BigDecimal(values[0].trim()),
                                        new BigDecimal(values[1].trim())
                                )
                        );
                    }

                    case JOIN -> {
                        validateJoin(filter.joinTable());

                        predicates.add(
                                criteriaBuilder.equal(
                                        root.join(filter.joinTable())
                                                .get(filter.column()),
                                        convertJoinValue(filter.column(), filter.value())
                                )
                        );
                    }
                }
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            ProductSearchRequest.GlobalOperator operator =
                    request.globalOperator() == null
                            ? ProductSearchRequest.GlobalOperator.AND
                            : request.globalOperator();

            if (operator == ProductSearchRequest.GlobalOperator.OR) {
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void validateColumn(String column) {
        if (!ALLOWED_COLUMNS.contains(column)) {
            throw new IllegalArgumentException(
                    "Column not allowed: " + column
            );
        }
    }

    private static void validateJoin(String joinTable) {
        if (!ALLOWED_JOINS.contains(joinTable)) {
            throw new IllegalArgumentException(
                    "Join not allowed: " + joinTable
            );
        }
    }

    private static Object convertValue(String column, String value) {
        return switch (column) {
            case "unitPrice" -> new BigDecimal(value);
            case "qty" -> Integer.parseInt(value);
            case "isAvailable", "isDeleted" -> Boolean.parseBoolean(value);
            default -> value;
        };
    }

    private static Object convertJoinValue(String column, String value) {
        if (column.equals("id")) {
            return Integer.parseInt(value);
        }

        return value;
    }
}
