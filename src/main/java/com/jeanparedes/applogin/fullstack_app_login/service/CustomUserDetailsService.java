package com.jeanparedes.applogin.fullstack_app_login.service;

import com.jeanparedes.applogin.fullstack_app_login.model.User;
import com.jeanparedes.applogin.fullstack_app_login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca al usuario en la base de datos por su email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Crea y devuelve un UserDetails que Spring Security utiliza para la autenticación y la autorización
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // Nombre de usuario
                .password(user.getPassword()) // Contraseña (ya está encriptada)
                .roles(user.getRole()) // Roles del usuario
                .build();
    }
}
