package com.kh.istad.ite.user.keycloak;

import com.kh.istad.fswd.attendance.common.exception.ApplicationException;
import com.kh.istad.ite.user.user.dto.CreateUserRequest;
import jakarta.ws.rs.core.Response; // note this
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class KeycloakUserClient {

    @Value("${app.keycloak.base-url}")
    private String baseUrl;

    @Value("${app.keycloak.realm}")
    private String realm;

    @Value("${app.keycloak.admin-realm}")
    private String adminRealm;

    @Value("${app.keycloak.admin-client-id}")
    private String adminClientId;

    @Value("${app.keycloak.admin-client-secret:}")
    private String adminClientSecret;

    @Value("${app.keycloak.admin-username}")
    private String adminUsername;

    @Value("${app.keycloak.admin-password}")
    private String adminPassword;

    public String createUser(CreateUserRequest request) {
        try (Keycloak keycloak = buildAdminClient()) {
            RealmResource realmResource = keycloak.realm(realm);
            UserRepresentation userRepresentation = toUserRepresentation(request);

            try (Response response = realmResource.users().create(userRepresentation)) {
                // Log
                log.info("Response status code: {}", response.getStatus());

                if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {

                    throw keycloakException("Cannot create Keycloak user: " + response.getStatusInfo().getReasonPhrase());
                }

                String userId = CreatedResponseUtil.getCreatedId(response);
                assignRealmRoles(realmResource, userId, request.roles());
                return userId;
            }
        } catch (ApplicationException exception) {
            throw exception;
        } catch (Exception exception) {
            throw keycloakException("Cannot create Keycloak user: " + exception.getMessage());
        }
    }

    public void deleteUser(String userId) {
        try (Keycloak keycloak = buildAdminClient()) {
            Response response = keycloak.realm(realm)
                    .users()
                    .delete(userId);

            try (response) {
                if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
                    throw keycloakException("Cannot delete Keycloak user: " + response.getStatusInfo().getReasonPhrase());
                }
            }
        } catch (ApplicationException exception) {
            throw exception;
        } catch (Exception exception) {
            throw keycloakException("Cannot delete Keycloak user: " + exception.getMessage());
        }
    }

    private Keycloak buildAdminClient() {
        KeycloakBuilder builder = KeycloakBuilder.builder()
                .serverUrl(baseUrl)
                .realm(adminRealm)
                .clientId(adminClientId);

        if (adminClientSecret != null && !adminClientSecret.isBlank()) {
            return builder
                    .clientSecret(adminClientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();
        }

        return builder
                .username(adminUsername)
                .password(adminPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    private UserRepresentation toUserRepresentation(CreateUserRequest request) {

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(request.userName());

        // this need set enable and verify
        userRepresentation.setEnabled(request.enabled() == null || request.enabled());
        userRepresentation.setEmailVerified(request.emailVerified() == null || request.emailVerified());
        userRepresentation.setEmail(request.email());

        userRepresentation.setFirstName(request.firstName());
        userRepresentation.setLastName(request.lastName());
        userRepresentation.setCredentials(List.of(toPasswordCredential(request)));

        userRepresentation.setAttributes(Map.of(
                "phone_number", List.of(nullToBlank(request.phone())),
                "address", List.of(nullToBlank(request.address())),
                "avatar", List.of(nullToBlank(request.avatar()))
        ));

        return userRepresentation;
    }

    private CredentialRepresentation toPasswordCredential(CreateUserRequest request) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.password());
        credential.setTemporary(Boolean.TRUE.equals(request.temporaryPassword()));
        return credential;
    }

    private void assignRealmRoles(RealmResource realmResource, String userId, List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return;
        }

        List<RoleRepresentation> roleRepresentations = new ArrayList<>();

        for (String role : roles) {
            if (role == null || role.isBlank()) {
                continue;
            }

            String normalizedRole = role.startsWith("ROLE_") ? role.substring("ROLE_".length()) : role;
            try {
                roleRepresentations.add(realmResource.roles().get(normalizedRole).toRepresentation());
            } catch (Exception exception) {
                throw keycloakException("Realm role not found or not accessible: " + normalizedRole);
            }
        }

        if (!roleRepresentations.isEmpty()) {
            realmResource.users().get(userId).roles().realmLevel().add(roleRepresentations);
        }
    }

    private String nullToBlank(String value) {
        return value == null ? "" : value;
    }

    private ApplicationException keycloakException(String message) {
        return new ApplicationException(HttpStatus.BAD_REQUEST, "KEYCLOAK_ERROR", message);
    }

}
