package com.kh.istad.ite.user.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateUserRequest(
        @NotBlank String userName,
        @NotBlank String password,
        @Email String email,
        String firstName,
        String lastName,
        String phone,
        String address,
        Boolean enabled,
        Boolean emailVerified,
        Boolean temporaryPassword,
        List<String> roles
) {
}
