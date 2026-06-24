package com.kh.istad.ite.user.user.repository;

import com.kh.istad.ite.user.user.domain.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<user, Long> {

    Optional<user> findByUuid(String uuid);
}
