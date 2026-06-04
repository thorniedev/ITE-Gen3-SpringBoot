package com.kh.istad.fswd.attendance.ecommerce.repository;

import com.kh.istad.fswd.attendance.ecommerce.domain.Order;
import com.kh.istad.fswd.attendance.ecommerce.domain.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Integer>
{

}
