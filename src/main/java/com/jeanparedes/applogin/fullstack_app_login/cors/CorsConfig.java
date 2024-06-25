package com.jeanparedes.applogin.fullstack_app_login.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configura todos los endpoints para permitir peticiones desde el origen http://localhost:4200
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // Permite solo este origen
                .allowedMethods("GET", "POST", "DELETE", "PUT"); // Métodos HTTP permitidos
    }
}
