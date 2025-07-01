package com.socialsentiment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Configuration class for web-specific settings in the application.
 *
 * This class implements the WebMvcConfigurer interface to customize
 * the configuration of the Spring MVC framework. In particular, it
 * configures Cross-Origin Resource Sharing (CORS) settings to enable
 * safe and controlled interaction between the backend and frontend.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings to allow
     * frontend applications to securely interact with the backend.
     *
     * This method allows all endpoints to be accessible from the specified
     * origin and permits all HTTP methods (e.g., GET, POST).
     *
     * @param registry the CorsRegistry to configure CORS mapping rules
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all endpoints
                .allowedOrigins("http://localhost:3000") // React frontend
                .allowedMethods("*"); // Allow GET, POST, etc.
    }
}