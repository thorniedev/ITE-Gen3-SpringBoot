package com.kh.istad.fswd.attendance.ecommerce.features.media;

import com.kh.istad.fswd.attendance.ecommerce.features.media.dto.ImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    ImageUploadResponse upload(MultipartFile file);
}
