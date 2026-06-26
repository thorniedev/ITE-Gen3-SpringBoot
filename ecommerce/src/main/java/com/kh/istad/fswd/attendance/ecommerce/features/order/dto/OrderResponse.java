package com.kh.istad.fswd.attendance.ecommerce.features.order.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String customerId,
        String address,
        Float discount,
        Boolean status,
        String phone,
        String email,
        String remark,
        LocalDate createdDate,
        Boolean isDeleted,
        PaymentQrResponse payment,
        List<OrderLineResponse> orderLines,
        BigDecimal subTotal,
        BigDecimal total
) {}
