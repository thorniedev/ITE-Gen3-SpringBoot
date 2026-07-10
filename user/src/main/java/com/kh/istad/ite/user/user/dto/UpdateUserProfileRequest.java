package com.kh.istad.ite.user.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String phone,

        String address,
        String avatar
) {
}
