package com.kh.istad.ite.user.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        String userName,

        @NotBlank
        @Size(min = 4, message = "Password must be at least 4 characters")
        String password,

        @NotBlank
        String confirmPassword,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String phone,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        String address,
        String avatar
) {
}
