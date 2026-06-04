package com.kh.istad.fswd.attendance.ecommerce.repository;

import com.kh.istad.fswd.attendance.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>
{ }
