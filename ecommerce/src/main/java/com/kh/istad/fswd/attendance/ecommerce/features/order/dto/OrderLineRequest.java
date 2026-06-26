package com.kh.istad.fswd.attendance.ecommerce.features.order.dto;

import jakarta.validation.constraints.*;

public record OrderLineRequest(

        @NotNull
        Integer productId,

        @NotNull
        @Positive
        Integer qty
) {}