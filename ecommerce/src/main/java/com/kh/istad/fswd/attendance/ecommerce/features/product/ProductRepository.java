package com.kh.istad.fswd.attendance.ecommerce.repository;

import com.kh.istad.fswd.attendance.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>,
        JpaSpecificationExecutor<Product>
{
    boolean  existsByName(String name);

    boolean existsByCode(String code);

    boolean existsBySlug(String slug);
}
