package com.kh.istad.fswd.attendance.ecommerce.features.order;

import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.CreateOrderRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.OrderResponse;
import com.kh.istad.ite.payment.paymentservice.dto.BakongResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request, String customerId);

    OrderResponse findById(UUID id);

    Page<OrderResponse> findAll(int pageNumber, int pageSize);

    OrderResponse cancel(UUID id);

    void softDelete(UUID id);

    void hardDelete(UUID id);

    OrderResponse updatePaymentStatus(UUID id, String paymentStatus);

    BakongResponse checkPayment(UUID id);
}
