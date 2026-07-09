package com.kh.istad.ite.user.auth.dto;

import java.util.List;

public record RegisterResponse(
        String userId,
        String userName,
        String firstName,
        String lastName,
        String avatar,
        String email,
        String phone,
        String address,
        List<String> roles
) {
}
