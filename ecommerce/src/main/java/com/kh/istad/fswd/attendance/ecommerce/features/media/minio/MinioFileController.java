package com.kh.istad.fswd.attendance.ecommerce.features.media.minio;

import com.kh.istad.fswd.attendance.ecommerce.features.media.local.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/minio")
@RequiredArgsConstructor
public class MinioFileController {

    private final MinioFileServiceImpl minioFileService;

    @PostMapping("/upload")
    public FileUploadResponse upload(
            @RequestParam("file") MultipartFile file
    ) {
        return minioFileService.upload(file);
    }

    @PostMapping(
            value = "/uploads",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public List<FileUploadResponse> uploadMultiple(
            @RequestParam("files") MultipartFile[] files
    ) {
        return minioFileService.uploadMultiple(files);
    }

    @DeleteMapping
    public void delete(@RequestParam String objectName) {
        minioFileService.delete(objectName);
    }
}