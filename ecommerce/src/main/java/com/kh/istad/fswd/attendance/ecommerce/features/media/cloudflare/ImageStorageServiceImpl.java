package com.kh.istad.fswd.attendance.ecommerce.features.media.cloudflare;

import com.kh.istad.fswd.attendance.common.exception.ApplicationException;
import com.kh.istad.fswd.attendance.common.exception.BadRequestException;
import com.kh.istad.fswd.attendance.ecommerce.features.media.cloudflare.dto.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageStorageServiceImpl implements ImageStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    private final S3Client s3Client;

    @Value("${cloudflare.r2.bucket-name:${cloudflare.r2.bucket}}")
    private String bucket;

    @Value("${cloudflare.r2.public-url:}")
    private String publicUrl;

    @Value("${cloudflare.r2.endpoint}")
    private String endpoint;

    @Value("${cloudflare.r2.image-folder:images}")
    private String imageFolder;

    @Value("${cloudflare.r2.max-image-size:5242880}")
    private long maxImageSize;

    @Override
    public ImageUploadResponse upload(MultipartFile file) {
        validate(file);
        validateR2Config();

        String contentType = file.getContentType();
        String key = generateKey(file.getOriginalFilename());

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(contentType)
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return new ImageUploadResponse(
                    key,
                    buildUrl(key),
                    file.getOriginalFilename(),
                    contentType,
                    file.getSize()
            );
        } catch (IOException | SdkException exception) {
            throw new ApplicationException(
                    HttpStatus.BAD_REQUEST,
                    "IMAGE_UPLOAD_FAILED",
                    "Image upload failed: " + exception.getMessage()
            );
        }
    }

    private void validateR2Config() {
        if (bucket == null || bucket.isBlank()) {
            throw new BadRequestException("Cloudflare R2 bucket name is required");
        }

        if (endpoint == null || endpoint.isBlank() || endpoint.contains("example.r2.cloudflarestorage.com")) {
            throw new BadRequestException("Cloudflare R2 endpoint is not configured");
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }

        if (file.getSize() > maxImageSize) {
            throw new BadRequestException("Image file is too large");
        }

        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new BadRequestException("Only JPEG, PNG, WEBP, and GIF images are allowed");
        }
    }

    private String generateKey(String originalFilename) {
        LocalDate now = LocalDate.now();
        String extension = getExtension(originalFilename);

        return "%s/%d/%02d/%s%s".formatted(
                cleanFolder(imageFolder),
                now.getYear(),
                now.getMonthValue(),
                UUID.randomUUID(),
                extension
        );
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) {
            return "";
        }

        int dotIndex = originalFilename.lastIndexOf('.');

        if (dotIndex < 0 || dotIndex == originalFilename.length() - 1) {
            return "";
        }

        return originalFilename.substring(dotIndex).toLowerCase(Locale.ROOT);
    }

    private String buildUrl(String key) {
        String baseUrl = publicUrl == null || publicUrl.isBlank()
                ? endpoint + "/" + bucket
                : publicUrl;

        return stripTrailingSlash(baseUrl) + "/" + key;
    }

    private String cleanFolder(String folder) {
        if (folder == null || folder.isBlank()) {
            return "images";
        }

        return stripTrailingSlash(folder).replaceAll("^/+", "");
    }

    private String stripTrailingSlash(String value) {
        return value.replaceAll("/+$", "");
    }
}
