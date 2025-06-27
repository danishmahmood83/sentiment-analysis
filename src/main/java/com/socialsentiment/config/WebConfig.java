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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all endpoints
                .allowedOrigins("http://localhost:3000") // React frontend
                .allowedMethods("*"); // Allow GET, POST, etc.
    }
}