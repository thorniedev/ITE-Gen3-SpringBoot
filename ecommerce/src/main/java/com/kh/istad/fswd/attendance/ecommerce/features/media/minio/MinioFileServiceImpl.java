package com.kh.istad.fswd.attendance.ecommerce.features.media.minio;

import com.kh.istad.fswd.attendance.ecommerce.features.media.local.dto.FileUploadResponse;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioFileServiceImpl implements MinioFileService{

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public FileUploadResponse upload(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size cannot exceed 5MB");
            }

            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());

            if (!originalFilename.contains(".")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file name");
            }

            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = "images/" + UUID.randomUUID() + ext;

            createBucketIfNotExist();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            String uri = endpoint + "/" + bucketName + "/" + objectName;

            return FileUploadResponse.builder()
                    .name(objectName)
                    .size(file.getSize())
                    .mediaType(file.getContentType())
                    .Uri(uri)
                    .build();

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Upload failed: " + e.getMessage()
            );
        }
    }

    @Override
    public List<FileUploadResponse> uploadMultiple(MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::upload)
                .toList();
    }

    @Override
    public void delete(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "File delete failed: " + e.getMessage()
            );
        }
    }

    // utils
    private void createBucketIfNotExist() throws Exception {
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        if (!exists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
        }
    }

//    public FileUploadResponse upload(MultipartFile file) {
//
//        try {
//            if (file.isEmpty()) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
//            }
//
//            if (file.getSize() > MAX_FILE_SIZE) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size cannot exceed 5MB");
//            }
//
//            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
//            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
//
//            String objectName = "images/" + UUID.randomUUID() + ext;
//
//            boolean exists = minioClient.bucketExists(
//                    BucketExistsArgs.builder()
//                            .bucket(bucketName)
//                            .build()
//            );
//
//            if (!exists) {
//                minioClient.makeBucket(
//                        MakeBucketArgs.builder()
//                                .bucket(bucketName)
//                                .build()
//                );
//            }
//
//            minioClient.putObject(
//                    PutObjectArgs.builder()
//                            .bucket(bucketName)
//                            .object(objectName)
//                            .stream(file.getInputStream(), file.getSize(), -1)
//                            .contentType(file.getContentType())
//                            .build()
//            );
//
//            String uri = endpoint + "/" + bucketName + "/" + objectName;
//
//            return FileUploadResponse.builder()
//                    .name(objectName)
//                    .size(file.getSize())
//                    .mediaType(file.getContentType())
//                    .Uri(uri)
//                    .build();
//
//        } catch (ResponseStatusException e) {
//            throw e;
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            throw new ResponseStatusException(
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    e.getClass().getSimpleName() + ": " + e.getMessage(),
//                    e
//            );
//        }
//    }
//
//    public List<FileUploadResponse> uploadMultiple(MultipartFile[] files) {
//        return List.of(files)
//                .stream()
//                .map(this::upload)
//                .toList();
//    }
//
//    public void delete(String objectName) {
//        try {
//            minioClient.removeObject(
//                    RemoveObjectArgs.builder()
//                            .bucket(bucketName)
//                            .object(objectName)
//                            .build()
//            );
//        } catch (Exception e) {
//            throw new ResponseStatusException(
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    "File delete failed"
//            );
//        }
//    }

}