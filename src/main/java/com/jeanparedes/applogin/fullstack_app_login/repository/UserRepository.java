package com.jeanparedes.applogin.fullstack_app_login.repository;

import com.jeanparedes.applogin.fullstack_app_login.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Método para buscar un usuario por su nombre de usuario
    User findByUsername(String username);

    // Método para buscar un usuario por su email
    Optional<User> findByEmail(String email);

}
