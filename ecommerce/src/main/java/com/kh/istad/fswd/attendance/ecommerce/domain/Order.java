package com.kh.istad.fswd.attendance.ecommerce.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String customerId;
    private String address;
    private Float discount;
    private Boolean status;
    private String phone;
    private String email;
    private String remark;
    private LocalDate createdDate;
    private Boolean isDeleted;

    // To orderLine
    @OneToMany(mappedBy = "order")
    private List<OrderLine> orderLines;
}
