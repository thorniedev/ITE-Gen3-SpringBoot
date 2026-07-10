package com.kh.istad.ite.user.user;

import com.kh.istad.fswd.attendance.common.exception.BadRequestException;
import com.kh.istad.fswd.attendance.common.exception.ResourceNotFoundException;
import com.kh.istad.ite.user.keycloak.KeycloakUserClient;
import com.kh.istad.ite.user.user.domain.User;
import com.kh.istad.ite.user.user.dto.CreateUserProfileRequest;
import com.kh.istad.ite.user.user.dto.UpdateUserProfileRequest;
import com.kh.istad.ite.user.user.dto.UserProfileResponse;
import com.kh.istad.ite.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private static final Set<String> APPLICATION_ROLES = Set.of("ADMIN", "SUPER_ADMIN", "CUSTOMER", "USER");

    private final UserRepository userRepository;
    private final KeycloakUserClient keycloakUserClient;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserProfileResponse syncCurrentUser(Jwt jwt) {
        User currentUser = userRepository.findById(jwt.getSubject())
                .orElseGet(User::new);

        currentUser.setUserId(jwt.getSubject());
        currentUser.setUserName(getClaim(jwt, "preferred_username"));
        currentUser.setFirstName(getClaim(jwt, "given_name"));
        currentUser.setLastName(getClaim(jwt, "family_name"));
        currentUser.setAvatar(getClaim(jwt, "avatar"));
        currentUser.setEmail(getClaim(jwt, "email"));
        currentUser.setPhone(getClaim(jwt, "phone_number"));
        currentUser.setAddress(getClaim(jwt, "address"));
        currentUser.setPassword(null);
        currentUser.setStatus(UserStatus.ACTIVE);

        currentUser = userRepository.save(currentUser);

        return userMapper.mapToUserProfileResponse(currentUser, extractRoles(jwt));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAllByStatusNot(UserStatus.DELETED)
                .stream()
                .map(userMapper::mapToUserProfileResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserById(String userId) {
        return userRepository.findByUserIdAndStatusNot(userId, UserStatus.DELETED)
                .map(userMapper::mapToUserProfileResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    @Override
    @Transactional
    public UserProfileResponse createUser(CreateUserProfileRequest request) {

        String keycloakUserId = keycloakUserClient.createUser(request);

        User newUser = new User();
        newUser.setUserId(keycloakUserId);
        newUser.setUserName(request.userName());
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setAvatar(request.avatar());
        newUser.setEmail(request.email());
        newUser.setPhone(request.phone());
        newUser.setAddress(request.address());
        newUser.setPassword(null);
        newUser.setStatus(Boolean.FALSE.equals(request.enabled()) ? UserStatus.DISABLED : UserStatus.ACTIVE);

        return userMapper.mapToUserProfileResponse(userRepository.save(newUser), request.roles());
    }

    @Override
    @Transactional
    public UserProfileResponse disableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        keycloakUserClient.disableUser(userId);
        user.setStatus(UserStatus.DISABLED);

        return userMapper.mapToUserProfileResponse(userRepository.save(user), List.of());
    }

    @Override
    @Transactional
    public UserProfileResponse deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (user.getStatus() == null || user.getStatus() == UserStatus.ACTIVE) {
            throw new BadRequestException("Disable user before hard delete");
        }

        UserProfileResponse response = userMapper.mapToUserProfileResponse(user, List.of());

        keycloakUserClient.deleteUser(userId);
        user.setStatus(UserStatus.DELETED);
        userRepository.delete(user);

        return response;
    }

    @Override
    public UserProfileResponse updateProfile(Jwt jwt, UpdateUserProfileRequest request) {

        String userId = jwt.getSubject();

        keycloakUserClient.updateUserProfile(userId, request);

        User user = userRepository.findById(userId)
                .orElseGet(User::new);

        user.setUserId(userId);
        user.setUserName(getClaim(jwt, "preferred_username"));
        user.setEmail(getClaim(jwt, "email"));
        user.setPassword(null);

        if (request.firstName() != null) {
            user.setFirstName(request.firstName());
        } else if (user.getFirstName() == null) {
            user.setFirstName(getClaim(jwt, "given_name"));
        }
        if (request.lastName() != null) {
            user.setLastName(request.lastName());
        } else if (user.getLastName() == null) {
            user.setLastName(getClaim(jwt, "family_name"));
        }
        if (request.phone() != null) {
            user.setPhone(request.phone());
        } else if (user.getPhone() == null) {
            user.setPhone(getClaim(jwt, "phone_number"));
        }
        if (request.address() != null) {
            user.setAddress(request.address());
        } else if (user.getAddress() == null) {
            user.setAddress(getClaim(jwt, "address"));
        }
        if (request.avatar() != null) {
            user.setAvatar(request.avatar());
        } else if (user.getAvatar() == null) {
            user.setAvatar(getClaim(jwt, "avatar"));
        }
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }

        return userMapper.mapToUserProfileResponse(userRepository.save(user), extractRoles(jwt));
    }

    private String getClaim(Jwt jwt, String claimName) {
        Object claim = jwt.getClaim(claimName);
        return claim == null ? null : claim.toString();
    }

    private List<String> extractRoles(Jwt jwt) {
        return extractRealmRoles(jwt).stream()
                .filter(APPLICATION_ROLES::contains)
                .toList();
    }

    private List<String> extractRealmRoles(Jwt jwt) {

        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null || !(realmAccess.get("roles") instanceof Collection<?> roles)) {
            return List.of();
        }

        return roles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(role -> role.startsWith("ROLE_") ? role.substring("ROLE_".length()) : role)
                .toList();
    }

}
