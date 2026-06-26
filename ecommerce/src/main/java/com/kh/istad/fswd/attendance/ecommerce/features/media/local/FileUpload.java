package com.kh.istad.fswd.attendance.ecommerce.features.media.local;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
public class FileUpload
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String caption;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String mediaType;

    private String extension;
}
