package com.kh.istad.ite.user.user;

import com.kh.istad.ite.user.keycloak.KeycloakUserClient;
import com.kh.istad.ite.user.user.domain.user;
import com.kh.istad.ite.user.user.dto.CreateUserRequest;
import com.kh.istad.ite.user.user.dto.UserProfileResponse;
import com.kh.istad.ite.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final KeycloakUserClient keycloakUserClient;

    @Override
    @Transactional
    public UserProfileResponse syncCurrentUser(Jwt jwt) {
        user currentUser = userRepository.findByUuid(jwt.getSubject())
                .orElseGet(user::new);

        currentUser.setUuid(jwt.getSubject());
        currentUser.setUserName(getClaim(jwt, "preferred_username"));
        currentUser.setEmail(getClaim(jwt, "email"));
        currentUser.setPhone(getClaim(jwt, "phone_number"));
        currentUser.setAddress(getClaim(jwt, "address"));
        currentUser.setPassword(null);

        currentUser = userRepository.save(currentUser);

        return mapToResponse(currentUser);
    }

    @Override
    @Transactional
    public UserProfileResponse createUser(CreateUserRequest request) {
        String keycloakUserId = keycloakUserClient.createUser(request);

        user newUser = new user();
        newUser.setUuid(keycloakUserId);
        newUser.setUserName(request.userName());
        newUser.setEmail(request.email());
        newUser.setPhone(request.phone());
        newUser.setAddress(request.address());
        newUser.setPassword(null);

        return mapToResponse(userRepository.save(newUser));
    }

    private String getClaim(Jwt jwt, String claimName) {
        Object claim = jwt.getClaim(claimName);
        return claim == null ? null : claim.toString();
    }

    private UserProfileResponse mapToResponse(user currentUser) {
        return new UserProfileResponse(
                currentUser.getId(),
                currentUser.getUuid(),
                currentUser.getUserName(),
                currentUser.getEmail(),
                currentUser.getPhone(),
                currentUser.getAddress()
        );
    }
}
