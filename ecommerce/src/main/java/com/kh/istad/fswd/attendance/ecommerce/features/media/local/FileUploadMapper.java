package com.kh.istad.fswd.attendance.ecommerce.features.media.local;

import com.kh.istad.fswd.attendance.ecommerce.features.media.local.dto.FileUploadResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUploadMapper {

    @Value("${file.base-uri}")
    private String baseUri;

    public FileUploadResponse mapFileUploadToFileUploadResponse(FileUpload fileUpload) {

        return FileUploadResponse.builder()
                .name(fileUpload.getName())
                .size(fileUpload.getSize())
                .mediaType(fileUpload.getMediaType())
                .Uri(baseUri + "/" + fileUpload.getName())
                .build();
    }
}
