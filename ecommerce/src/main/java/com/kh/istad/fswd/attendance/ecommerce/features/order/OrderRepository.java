package com.kh.istad.fswd.attendance.ecommerce.features.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>
{
    Optional<Order> findByIdAndIsDeletedFalse(UUID id);

    Page<Order> findByIsDeletedFalse(Pageable pageable);

}
