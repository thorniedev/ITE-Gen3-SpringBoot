package com.kh.istad.fswd.attendance.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(length = 100, nullable = false, unique = true)
    private String slug;

    @Column(nullable = true)
    private String thumbnail;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = true)
    private String description;

    private Boolean isAvailable;
    private Boolean isDeleted; // soft delete

    // relationship with category
    @ManyToOne()
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<OrderLine> orderLines;

}
