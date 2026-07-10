package com.kh.istad.ite.user.user;

import com.kh.istad.ite.user.user.dto.UpdateUserProfileRequest;
import com.kh.istad.ite.user.user.dto.UserProfileResponse;
import com.kh.istad.ite.user.user.dto.CreateUserProfileRequest;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface UserProfileService {

    UserProfileResponse syncCurrentUser(Jwt jwt);

    List<UserProfileResponse> getAllUsers();

    UserProfileResponse getUserById(String userId);

    UserProfileResponse createUser(CreateUserProfileRequest request);

    UserProfileResponse disableUser(String userId);

    UserProfileResponse deleteUser(String userId);

    UserProfileResponse updateProfile(Jwt jwt, UpdateUserProfileRequest request);
}
