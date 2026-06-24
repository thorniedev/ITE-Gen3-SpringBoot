package com.kh.istad.ite.user.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        addRealmRoles(jwt, authorities);
        addClientRoles(jwt, authorities);

        return new JwtAuthenticationToken(jwt, authorities, getPrincipalName(jwt));
    }

    private void addRealmRoles(Jwt jwt, Set<GrantedAuthority> authorities) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null) {
            return;
        }

        addRoles(realmAccess.get("roles"), authorities);
    }

    private void addClientRoles(Jwt jwt, Set<GrantedAuthority> authorities) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess == null) {
            return;
        }

        resourceAccess.values().stream()
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .map(clientAccess -> clientAccess.get("roles"))
                .forEach(roles -> addRoles(roles, authorities));
    }

    private void addRoles(Object rolesClaim, Set<GrantedAuthority> authorities) {
        if (!(rolesClaim instanceof Collection<?> roles)) {
            return;
        }

        roles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);
    }

    private String getPrincipalName(Jwt jwt) {
        List<String> preferredClaims = List.of("preferred_username", "email", "sub");

        return preferredClaims.stream()
                .map(jwt::getClaimAsString)
                .filter(claim -> claim != null && !claim.isBlank())
                .findFirst()
                .orElse(jwt.getSubject());
    }
}
