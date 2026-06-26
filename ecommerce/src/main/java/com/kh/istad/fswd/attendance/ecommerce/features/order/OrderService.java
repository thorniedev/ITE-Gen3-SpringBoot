package com.kh.istad.fswd.attendance.ecommerce.features.order;

import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.OrderRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.order.dto.OrderResponse;
import com.kh.istad.ite.payment.paymentservice.dto.BakongResponse;

import java.util.UUID;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);

    OrderResponse findById(UUID id);

    BakongResponse checkPayment(UUID id);
}
