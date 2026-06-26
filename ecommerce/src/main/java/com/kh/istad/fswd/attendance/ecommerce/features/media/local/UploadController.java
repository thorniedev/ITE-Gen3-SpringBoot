package com.kh.istad.fswd.attendance.ecommerce.features.media.local;

import com.kh.istad.fswd.attendance.ecommerce.features.media.local.dto.DeleteFileRequest;
import com.kh.istad.fswd.attendance.ecommerce.features.media.local.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class UploadController
{

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public FileUploadResponse upload(@RequestPart("file") MultipartFile file) throws IOException {
        return fileUploadService.upload(file);
    }

    @PostMapping(
            value = "/uploads",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public List<FileUploadResponse> uploadMultiple(
            @RequestPart("files")
            MultipartFile[] files) throws IOException {

        return fileUploadService.uploadMultiple(List.of(files));
    }

    @GetMapping
    public Page<FileUploadResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return fileUploadService.findAll(page, size);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public FileUploadResponse findByName(
            @RequestParam String name
    ) {
        return fileUploadService.findByName(name);
    }

    //    // This only delete in folder
    //    @DeleteMapping()
    //    private void deleteFile(@RequestParam String name) throws IOException {
    //
    //        fileUploadService.deleteFile(name);
    //
    //    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFileByName(
            @RequestBody DeleteFileRequest request
    ) throws IOException {
        fileUploadService.deleteFileByName(request.name());
    }

}
