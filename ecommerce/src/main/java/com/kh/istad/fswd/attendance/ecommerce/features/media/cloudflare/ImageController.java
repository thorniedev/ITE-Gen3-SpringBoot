package com.kh.istad.fswd.attendance.ecommerce.features.media.cloudflare;

import com.kh.istad.fswd.attendance.ecommerce.features.media.cloudflare.dto.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageStorageService imageStorageService;

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ImageUploadResponse upload(@RequestPart("file") MultipartFile file) {
        return imageStorageService.upload(file);
    }
}
