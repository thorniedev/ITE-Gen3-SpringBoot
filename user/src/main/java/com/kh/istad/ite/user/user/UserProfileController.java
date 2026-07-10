package com.kh.istad.ite.user.user;

import com.kh.istad.ite.user.user.dto.CreateUserProfileRequest;
import com.kh.istad.ite.user.user.dto.UpdateUserProfileRequest;
import com.kh.istad.ite.user.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserProfileResponse> getAllUsers() {
        return userProfileService.getAllUsers();
    }

    @GetMapping("/me")
    public UserProfileResponse getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return userProfileService.syncCurrentUser(jwt);
    }

    @GetMapping("/{userId}")
    public UserProfileResponse getUserById(@PathVariable("userId") String userId) {
        return userProfileService.getUserById(userId);
    }

    @PatchMapping("/me")
    public UserProfileResponse updateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        return userProfileService.updateProfile(jwt, request);
    }


    @PostMapping
    public UserProfileResponse createUser(
            @Valid
            @RequestBody CreateUserProfileRequest request
    ) {
        return userProfileService.createUser(request);
    }

    @PutMapping("/{userId}/disable")
    public UserProfileResponse disableUser(
            @PathVariable("userId") String userId
    ) {
        return userProfileService.disableUser(userId);
    }

    @DeleteMapping("/delete/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserProfileResponse deleteUser(
            @PathVariable("userId") String userId
    ){
        return userProfileService.deleteUser(userId);
    }
}
