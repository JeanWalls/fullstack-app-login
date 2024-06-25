package com.jeanparedes.applogin.fullstack_app_login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jeanparedes.applogin.fullstack_app_login.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    SecurityFilter securityFilter;

    // Configura la cadena de filtros de seguridad HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilita la protección CSRF
                .csrf(csrf -> csrf.disable())
                // Configura la gestión de sesiones como STATELESS (sin estado)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura las reglas de autorización HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Permite el acceso público al endpoint /auth/login mediante POST
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        // Permite el acceso público al endpoint /auth/register mediante POST
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        // Requiere autenticación para cualquier otra solicitud HTTP
                        .anyRequest().authenticated())
                // Configura el servicio personalizado de detalles del usuario
                .userDetailsService(userDetailsService)
                // Agrega el filtro de seguridad personalizado antes del filtro de autenticación por usuario y contraseña
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Configura el codificador de contraseñas BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configura el administrador de autenticación
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
