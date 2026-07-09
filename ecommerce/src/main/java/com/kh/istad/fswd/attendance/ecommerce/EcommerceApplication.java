package com.kh.istad.fswd.attendance.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication(
  scanBasePackages = "com.kh.istad"
)


@EntityScan(basePackages = "com.kh.istad")
@EnableJpaRepositories(basePackages = "com.kh.istad")
@EnableConfigurationProperties
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

}
