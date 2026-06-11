package com.kh.istad.fswd.attendance.ecommerce.dto.orderline;

import java.math.BigDecimal;

public record OrderLineResponse(
        Integer id,
        Integer qty,
        BigDecimal unitPrice,
        Integer productId,
        String productName,
        BigDecimal lineTotal
) {}