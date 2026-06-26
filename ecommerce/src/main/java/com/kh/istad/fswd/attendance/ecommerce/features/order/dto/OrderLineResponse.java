package com.kh.istad.fswd.attendance.ecommerce.features.order.dto;

import java.math.BigDecimal;

public record OrderLineResponse(
        Integer id,
        Integer qty,
        BigDecimal unitPrice,
        Integer productId,
        String productName,
        BigDecimal lineTotal
) {}