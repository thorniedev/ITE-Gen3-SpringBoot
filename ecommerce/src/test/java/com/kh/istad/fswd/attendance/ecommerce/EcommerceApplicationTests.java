package com.kh.istad.fswd.attendance.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:ecommerce_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost/realms/test",
        "app.keycloak.base-url=http://localhost",
        "app.keycloak.realm=ite",
        "app.keycloak.admin-realm=ite",
        "app.keycloak.admin-client-id=admin-cli",
        "app.keycloak.admin-client-secret=",
        "app.keycloak.admin-username=admin",
        "app.keycloak.admin-password=password",
        "bakong.account-id=test@bank",
        "bakong.base-url=http://localhost",
        "bakong.api-token=test-token",
        "bakong.email=test@example.com"
})
class EcommerceApplicationTests {

    @Test
    void contextLoads() {
    }

}
