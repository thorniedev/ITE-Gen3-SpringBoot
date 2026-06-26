package com.kh.istad.fswd.attendance.ecommerce.features.media.local;

import com.kh.istad.fswd.attendance.ecommerce.features.media.local.dto.FileUploadResponse;
import com.kh.istad.fswd.attendance.ecommerce.features.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploadService {

    /**
     * Upload a new file
     * @param file is requesting data for uploading file
     * @return {@link FileUploadResponse}
     * @author kim chanthorn
     * @since June 24, 2026
     */
    FileUploadResponse upload(MultipartFile file) throws IOException;

    /**
     * Upload multiple files
     * @param files is requesting data for uploading files
     * @return {@link FileUploadResponse}
     * @author kim chanthorn
     * @since June 24, 2026
     */
    //List<FileUploadResponse> uploadMultiple(MultipartFile[] files) throws IOException;
    List<FileUploadResponse> uploadMultiple(List<MultipartFile> files) throws IOException;

    FileUploadResponse findByName(String name);

    Page<FileUploadResponse> findAll(int pageNumber, int pageSize);

    /**
     * Delete a file
     * @param fileName is requesting data for uploading files
     * @author kim chanthorn
     * @since June 24, 2026
     */
    void deleteFile(String fileName) throws IOException;

    void deleteFileByName(String fileName) throws IOException;

}
