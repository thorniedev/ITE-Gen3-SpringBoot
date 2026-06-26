package com.kh.istad.fswd.attendance.ecommerce.features.media.minio;

import com.kh.istad.fswd.attendance.ecommerce.features.media.local.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MinioFileService {

    FileUploadResponse upload(MultipartFile file);

    List<FileUploadResponse> uploadMultiple(MultipartFile[] files);

    void delete(String objectName);
}
