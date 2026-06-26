package com.kh.istad.fswd.attendance.ecommerce.features.media.local;

import com.kh.istad.fswd.attendance.ecommerce.features.media.local.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {


    private final FileUploadRepository fileUploadRepository;

    private final FileUploadMapper fileUploadMapper;

    @Value("${file.storage-location}")
    private String storageLocation;

    @Value("${file.base-uri}")
    private String baseUri;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int MAX_FILES = 10;

    @Override
    public FileUploadResponse upload(MultipartFile file) throws IOException {

        // validate before upload
//        if (file.isEmpty()) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "File is empty"
//            );
//        }
//        if (file.getSize() > MAX_FILE_SIZE) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "File size cannot exceed 5MB"
//            );
//        }
//
//        // Prepare file information
//        // File name
//        // Generate: - filename+LocalDateTime+origin+...
//        //           - Uuid
//
//        String name = UUID.randomUUID().toString();
//
//        // myprofile.png (sub-string last dot(.) )
//        String ext = Objects.requireNonNull(file.getOriginalFilename())
//               .substring(file.getOriginalFilename().lastIndexOf("."));
//
//        name += ext; // new unique filename.ext
//
//        // New for avoid (file/***)
//        String fileUri = String.format("%s/%s", baseUri, name);
//
//        // create absolute path to store file
//        Path path = Paths.get(storageLocation + name);
//
//        try {
//            Files.copy(file.getInputStream(), path);
//        } catch (IOException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
//                    "File has been failed to upload"
//                    );
//        }

        return saveFile(file);
    }

    @Override
    public List<FileUploadResponse> uploadMultiple(List<MultipartFile> files) throws IOException {

        return files.stream()
                .map(file -> {
                    try {
                        return saveFile(file);
                    } catch (IOException e) {
                        throw new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "File has been failed to upload"
                        );
                    }
                })
                .toList();


        //        if (files.length > MAX_FILES) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Maximum 10 files allowed"
//            );
//        }
//
//        return Arrays.stream(files)
//                .map(file -> {
//                    try {
//                        return upload(file);
//                    } catch (IOException e) {
//                        throw new ResponseStatusException(
//                                HttpStatus.INTERNAL_SERVER_ERROR,
//                                "File has been failed to upload"
//                        );
//                    }
//                })
//                .toList();

            //                    List<FileUploadResponse> responses = new ArrayList<>();
            //
            //                    for (MultipartFile file : files) {
            //                        responses.add(upload(file));
            //                    }
            //
            //                    return responses;

    }

    @Override
    public Page<FileUploadResponse> findAll(int pageNumber, int pageSize) {
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortById);

        Page<FileUpload> fileUploadResponses = fileUploadRepository.findAll(pageRequest);
        return fileUploadRepository.findAll(pageRequest)
                .map(fileUploadMapper::mapFileUploadToFileUploadResponse);
    }

    @Override
    public FileUploadResponse findByName(String name) {

        FileUpload fileUpload = fileUploadRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "File not found: " + name
                ));

        return fileUploadMapper.mapFileUploadToFileUploadResponse(fileUpload);
    }

    @Override
    public void deleteFile(String fileName) throws IOException {
        Path path = Paths.get(storageLocation)
                .resolve(fileName)
                .normalize();

        if (!Files.exists(path)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "File not found: " + fileName
            );
        }

        Files.delete(path);
    }

    @Transactional
    @Override
    public void deleteFileByName(String fileName) throws IOException {
        FileUpload fileUpload = fileUploadRepository.findByName(fileName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "File not found In database: " + fileName
                ));

        Path path = Paths.get(storageLocation)
                .resolve(fileName)
                .normalize();
        if (!Files.exists(path)) {
            // delete DB even physical file missing
            fileUploadRepository.delete(fileUpload);

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "File not found In folder: " + fileName);
        }

        Files.delete(path);

        fileUploadRepository.delete(fileUpload);

    }

    private FileUploadResponse saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size cannot exceed 5MB");
        }

        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());

        String ext = "";
        int lastDot = originalFilename.lastIndexOf(".");
        if (lastDot > 0) {
            ext = originalFilename.substring(lastDot);
        }

        String name = UUID.randomUUID() + ext;

        Path uploadPath = Paths.get(storageLocation);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path path = uploadPath.resolve(name).normalize();

        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "File has been failed to upload"
            );
        }

        FileUpload fileUpload = new FileUpload();
        fileUpload.setName(name);
        fileUpload.setExtension(ext);
        fileUpload.setCaption("ISTAD IMAGE");
        fileUpload.setSize(file.getSize());
        fileUpload.setMediaType(file.getContentType());

        fileUploadRepository.save(fileUpload);

        String fileUri = String.format("%s/%s", baseUri, name);

        return FileUploadResponse.builder()
                .name(name)
                .size(file.getSize())
                .mediaType(file.getContentType())
                //.Uri(baseUri + "/" + name)
                .Uri(fileUri)
                .build();
    }

}
