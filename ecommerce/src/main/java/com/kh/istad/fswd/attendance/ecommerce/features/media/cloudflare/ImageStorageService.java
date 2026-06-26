package com.kh.istad.fswd.attendance.ecommerce.features.media.cloudflare;

import com.kh.istad.fswd.attendance.ecommerce.features.media.cloudflare.dto.ImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    ImageUploadResponse upload(MultipartFile file);
}
