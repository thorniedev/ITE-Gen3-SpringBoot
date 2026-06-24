package com.kh.istad.fswd.attendance.ecommerce.dto.orderline;

import jakarta.validation.constraints.*;

public record OrderLineRequest(
        @NotNull Integer productId,
        @NotNull @Positive Integer qty
) {}