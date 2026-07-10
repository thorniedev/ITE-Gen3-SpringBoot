package com.kh.istad.ite.user.user.dto;

import com.kh.istad.ite.user.user.UserStatus;

import java.util.List;

public record UserProfileResponse(
        String userId,
        String userName,
        String firstName,
        String lastName,
        String avatar,
        String email,
        String phone,
        String address,
        UserStatus status,
        List<String> roles
) {
}
