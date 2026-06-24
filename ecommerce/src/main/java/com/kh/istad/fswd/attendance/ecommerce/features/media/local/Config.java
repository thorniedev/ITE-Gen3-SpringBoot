package com.kh.istad.fswd.attendance.ecommerce.features.media;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer
{
    @Value("${file.storage-location}")
    private String fileStorageLocation;

    @Value("${file.client-path}")
    private String classPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(classPath + "/**")
                .addResourceLocations("file:" + fileStorageLocation);
    }
}
