package com.kh.istad.ite.user.keycloak;

import com.kh.istad.fswd.attendance.common.exception.ApplicationException;
import com.kh.istad.ite.user.user.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KeycloakUserClient {

    private final RestClient.Builder restClientBuilder;

    @Value("${app.keycloak.base-url}")
    private String baseUrl;

    @Value("${app.keycloak.realm}")
    private String realm;

    @Value("${app.keycloak.admin-realm}")
    private String adminRealm;

    @Value("${app.keycloak.admin-client-id}")
    private String adminClientId;

    @Value("${app.keycloak.admin-username}")
    private String adminUsername;

    @Value("${app.keycloak.admin-password}")
    private String adminPassword;

    public String createUser(CreateUserRequest request) {
        String accessToken = getAdminAccessToken();

        try {
            HttpHeaders headers = restClientBuilder.build()
                    .post()
                    .uri(baseUrl + "/admin/realms/{realm}/users", realm)
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                    .body(toKeycloakUserBody(request))
                    .retrieve()
                    .toBodilessEntity()
                    .getHeaders();

            String userId = extractUserId(headers);
            assignRealmRoles(userId, request.roles(), accessToken);

            return userId;
        } catch (RestClientResponseException exception) {
            throw keycloakException("Cannot create Keycloak user: " + exception.getResponseBodyAsString());
        }
    }

    private String getAdminAccessToken() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", adminClientId);
        body.add("username", adminUsername);
        body.add("password", adminPassword);

        try {
            Map<String, Object> response = restClientBuilder.build()
                    .post()
                    .uri(baseUrl + "/realms/{realm}/protocol/openid-connect/token", adminRealm)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (response == null || response.get("access_token") == null) {
                throw keycloakException("Cannot get Keycloak admin access token");
            }

            return response.get("access_token").toString();
        } catch (RestClientResponseException exception) {
            throw keycloakException("Cannot get Keycloak admin access token: " + exception.getResponseBodyAsString());
        }
    }

    private Map<String, Object> toKeycloakUserBody(CreateUserRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("username", request.userName());
        body.put("enabled", request.enabled() == null || request.enabled());
        body.put("emailVerified", Boolean.TRUE.equals(request.emailVerified()));

        putIfNotBlank(body, "email", request.email());
        putIfNotBlank(body, "firstName", request.firstName());
        putIfNotBlank(body, "lastName", request.lastName());

        Map<String, List<String>> attributes = new LinkedHashMap<>();
        putAttributeIfNotBlank(attributes, "phone_number", request.phone());
        putAttributeIfNotBlank(attributes, "address", request.address());

        if (!attributes.isEmpty()) {
            body.put("attributes", attributes);
        }

        body.put("credentials", List.of(Map.of(
                "type", "password",
                "value", request.password(),
                "temporary", Boolean.TRUE.equals(request.temporaryPassword())
        )));

        return body;
    }

    private void assignRealmRoles(String userId, List<String> roles, String accessToken) {
        if (roles == null || roles.isEmpty()) {
            return;
        }

        List<Map<String, Object>> roleRepresentations = new ArrayList<>();

        for (String role : roles) {
            roleRepresentations.add(getRealmRole(role, accessToken));
        }

        restClientBuilder.build()
                .post()
                .uri(baseUrl + "/admin/realms/{realm}/users/{userId}/role-mappings/realm", realm, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .body(roleRepresentations)
                .retrieve()
                .toBodilessEntity();
    }

    private Map<String, Object> getRealmRole(String role, String accessToken) {
        return restClientBuilder.build()
                .get()
                .uri(baseUrl + "/admin/realms/{realm}/roles/{role}", realm, role)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    private String extractUserId(HttpHeaders headers) {
        URI location = headers.getLocation();

        if (location == null) {
            throw keycloakException("Keycloak did not return created user location");
        }

        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private void putIfNotBlank(Map<String, Object> body, String key, String value) {
        if (value != null && !value.isBlank()) {
            body.put(key, value);
        }
    }

    private void putAttributeIfNotBlank(Map<String, List<String>> attributes, String key, String value) {
        if (value != null && !value.isBlank()) {
            attributes.put(key, List.of(value));
        }
    }

    private ApplicationException keycloakException(String message) {
        return new ApplicationException(HttpStatus.BAD_REQUEST, "KEYCLOAK_ERROR", message);
    }

}
