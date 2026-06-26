package com.kh.istad.fswd.attendance.ecommerce.features.order.dto;

public record PaymentQrResponse(
        String status,
        String md5,
        String qr
) {
}
