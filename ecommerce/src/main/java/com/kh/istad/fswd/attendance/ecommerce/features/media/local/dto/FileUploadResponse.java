package com.kh.istad.fswd.attendance.ecommerce.features.media.local.dto;

import lombok.Builder;

@Builder
public record FileUploadResponse(
    String name,
    String caption,
    Long size,
    String mediaType,
    String Uri,
    String downloadUri
) { }
