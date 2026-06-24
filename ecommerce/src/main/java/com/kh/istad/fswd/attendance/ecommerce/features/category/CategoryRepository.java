package com.kh.istad.fswd.attendance.ecommerce.repository;

import com.kh.istad.fswd.attendance.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>

{
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);

    List<Category> findByIsDeletedFalse();

    Optional<Category> findByIdAndIsDeletedFalse(Integer id);

    // For find parent category
    List<Category> findByParentCategoryId(Integer parentCategoryId);

    boolean existsByParentCategoryId(Integer parentCategoryId);
}
