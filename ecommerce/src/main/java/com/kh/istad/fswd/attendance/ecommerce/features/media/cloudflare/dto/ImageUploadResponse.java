package com.kh.istad.fswd.attendance.ecommerce.features.media.cloudflare.dto;

public record ImageUploadResponse(
        String key,
        String url,
        String originalName,
        String contentType,
        Long size
) {
}
