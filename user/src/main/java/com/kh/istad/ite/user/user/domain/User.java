package com.kh.istad.ite.user.user.domain;

import com.kh.istad.ite.user.user.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User
{
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId; // from Keycloak

    private String avatar; // new column

    @Column(unique = true)
    private String userName; //

    private String firstName; //
    private String lastName; //
    private String password; //
    private String email; //
    private String phone; //
    private String address; //

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'ACTIVE'")
    private UserStatus status; // [ACTIVE, DISABLED, DELETED]

    @PrePersist
    @PreUpdate
    @PostLoad
    private void setDefaultStatus() {
        if (status == null) {
            status = UserStatus.ACTIVE;
        }
    }

}
