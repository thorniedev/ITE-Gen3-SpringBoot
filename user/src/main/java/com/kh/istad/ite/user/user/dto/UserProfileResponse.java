package com.kh.istad.ite.user.user.dto;

public record UserProfileResponse(
        Long id,
        String uuid,
        String userName,
        String email,
        String phone,
        String address
) {
}
