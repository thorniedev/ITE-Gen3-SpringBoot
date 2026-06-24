package com.kh.istad.fswd.attendance.ecommerce.repository;

import com.kh.istad.fswd.attendance.ecommerce.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Integer>
{
    List<OrderLine> findByOrder_Id(UUID orderId);
}
