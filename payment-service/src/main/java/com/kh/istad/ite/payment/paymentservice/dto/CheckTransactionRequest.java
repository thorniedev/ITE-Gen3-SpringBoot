package com.kh.istad.ite.payment.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CheckTransactionRequest(
        @NotBlank
        String md5
) {
}
