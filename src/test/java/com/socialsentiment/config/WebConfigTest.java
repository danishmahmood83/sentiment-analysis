package com.socialsentiment.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistration;

import static org.mockito.Mockito.*;

public class WebConfigTest {

    @Test
    void testAddCorsMappings() {
        WebConfig config = new WebConfig();
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);

        when(registry.addMapping("/**")).thenReturn(registration);
        when(registration.allowedOrigins("http://localhost:3000")).thenReturn(registration);
        when(registration.allowedMethods("*")).thenReturn(registration);

        config.addCorsMappings(registry);

        verify(registry).addMapping("/**");
        verify(registration).allowedOrigins("http://localhost:3000");
        verify(registration).allowedMethods("*");
    }
}
