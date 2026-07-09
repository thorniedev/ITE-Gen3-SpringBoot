package com.kh.istad.ite.user.user;

import com.kh.istad.fswd.attendance.common.exception.ResourceNotFoundException;
import com.kh.istad.ite.user.keycloak.KeycloakUserClient;
import com.kh.istad.ite.user.user.domain.User;
import com.kh.istad.ite.user.user.dto.CreateUserRequest;
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

        currentUser = userRepository.save(currentUser);

        return userMapper.mapToUserProfileResponse(currentUser, extractRoles(jwt));
    }

    @Override
    @Transactional
    public UserProfileResponse createUser(CreateUserRequest request) {

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

        return userMapper.mapToUserProfileResponse(userRepository.save(newUser), request.roles());
    }

    @Override
    @Transactional
    public UserProfileResponse deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        UserProfileResponse response = userMapper.mapToUserProfileResponse(user, List.of());

        keycloakUserClient.deleteUser(userId);
        userRepository.delete(user);

        return response;
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
