package com.kh.istad.fswd.attendance.ecommerce.features.media.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

    Optional<FileUpload> findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);

}
