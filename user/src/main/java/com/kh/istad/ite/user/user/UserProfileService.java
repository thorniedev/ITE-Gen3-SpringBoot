package com.kh.istad.ite.user.user;

import com.kh.istad.ite.user.user.dto.UserProfileResponse;
import com.kh.istad.ite.user.user.dto.CreateUserRequest;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserProfileService {

    UserProfileResponse syncCurrentUser(Jwt jwt);

    UserProfileResponse createUser(CreateUserRequest request);

    UserProfileResponse deleteUser(String userId);
}
