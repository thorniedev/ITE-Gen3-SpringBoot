package com.kh.istad.fswd.attendance.ecommerce.features.order.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePaymentStatusRequest(

        @NotBlank
        String paymentStatus

) {
}
