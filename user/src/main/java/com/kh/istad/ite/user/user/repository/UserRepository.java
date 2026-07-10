package com.kh.istad.ite.user.user.repository;

import com.kh.istad.ite.user.user.UserStatus;
import com.kh.istad.ite.user.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.status is null or u.status <> :status")
    List<User> findAllByStatusNot(@Param("status") UserStatus status);

    @Query("select u from User u where u.userId = :userId and (u.status is null or u.status <> :status)")
    Optional<User> findByUserIdAndStatusNot(@Param("userId") String userId, @Param("status") UserStatus status);
}
