package com.kh.istad.ite.user.user;

import com.kh.istad.ite.user.user.dto.UserProfileResponse;
import com.kh.istad.ite.user.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // map from Response to Entity
    @Mapping(target = "password", ignore = true)
    User mapToUser(UserProfileResponse userProfileResponse);

    // map from entity to response
    @Mapping(target = "roles", expression = "java(java.util.List.of())")
    UserProfileResponse mapToUserProfileResponse(User user);

    @Mapping(target = "roles", source = "roles")
    UserProfileResponse mapToUserProfileResponse(User user, List<String> roles);

}
