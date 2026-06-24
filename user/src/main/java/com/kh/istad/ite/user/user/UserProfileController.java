package com.kh.istad.ite.user.user;

import com.kh.istad.ite.user.user.dto.CreateUserRequest;
import com.kh.istad.ite.user.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public UserProfileResponse getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return userProfileService.syncCurrentUser(jwt);
    }

    @PostMapping
    public UserProfileResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return userProfileService.createUser(request);
    }
}
