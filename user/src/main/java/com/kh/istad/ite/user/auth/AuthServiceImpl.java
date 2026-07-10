package com.kh.istad.ite.user.auth;

import com.kh.istad.fswd.attendance.common.exception.BadRequestException;
import com.kh.istad.ite.user.auth.dto.RegisterRequest;
import com.kh.istad.ite.user.auth.dto.RegisterResponse;
import com.kh.istad.ite.user.user.UserProfileService;
import com.kh.istad.ite.user.user.dto.CreateUserProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService
{
    private static final List<String> REGISTER_ROLES = List.of("CUSTOMER", "USER");

    private final UserProfileService userProfileService;
    private final AuthMapper authMapper;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (!registerRequest.password().equals(registerRequest.confirmPassword())) {
            throw new BadRequestException("Password and confirm password do not match");
        }

        CreateUserProfileRequest createUserProfileRequest = new CreateUserProfileRequest(
                registerRequest.userName(),
                registerRequest.password(),
                registerRequest.email(),
                registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.phone(),
                registerRequest.address(),
                registerRequest.avatar(),
                true,
                true,
                false,
                REGISTER_ROLES
        );

        return authMapper.mapToRegisterResponse(userProfileService.createUser(createUserProfileRequest));
    }

}
