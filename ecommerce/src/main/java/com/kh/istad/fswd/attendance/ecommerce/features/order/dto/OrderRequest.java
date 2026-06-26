package com.kh.istad.fswd.attendance.ecommerce.features.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;

import java.util.List;

public record OrderRequest(
        @NotBlank String customerId,
        @NotBlank String address,
        @PositiveOrZero Float discount,
        String phone,
        @Email String email,
        String remark,
        KHQRCurrency paymentCurrency,
        @NotEmpty @Valid List<OrderLineRequest> orderLines
) {}
