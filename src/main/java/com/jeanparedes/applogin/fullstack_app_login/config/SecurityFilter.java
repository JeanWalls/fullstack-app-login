package com.jeanparedes.applogin.fullstack_app_login.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jeanparedes.applogin.fullstack_app_login.model.User;
import com.jeanparedes.applogin.fullstack_app_login.repository.UserRepository;
import com.jeanparedes.applogin.fullstack_app_login.service.TokenService;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService; // Servicio para manejar tokens JWT

    @Autowired
    UserRepository userRepository; // Repositorio para acceder a los datos de usuario

    // Método principal para aplicar la lógica de seguridad en cada solicitud HTTP
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request); // Recupera el token JWT del encabezado Authorization
        var login = tokenService.validateToken(token); // Valida el token JWT y devuelve el email del usuario

        if(login != null){
            // Si el token es válido, busca al usuario en la base de datos por su email
            User user = userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User Not Found"));

            // Crea la lista de roles (en este caso, solo ROLE_USER)
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

            // Crea el objeto de autenticación con el usuario y los roles
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

            // Establece la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continúa con el filtro siguiente en la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Método para recuperar el token JWT del encabezado Authorization
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
