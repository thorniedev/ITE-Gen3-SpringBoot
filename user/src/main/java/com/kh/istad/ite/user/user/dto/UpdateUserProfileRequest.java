package com.kh.istad.ite.user.user.dto;

public record UpdateUserProfileRequest(
        String firstName,
        String lastName,
        String phone,
        String address,
        String avatar
) {
}
