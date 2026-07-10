package com.kh.istad.ite.user.auth;

import com.kh.istad.ite.user.auth.dto.RegisterResponse;
import com.kh.istad.ite.user.user.dto.UserProfileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    RegisterResponse mapToRegisterResponse(UserProfileResponse userProfileResponse);
}
