package com.jeanparedes.applogin.fullstack_app_login.controller;

import com.jeanparedes.applogin.fullstack_app_login.DTO.LoginRequestDTO;
import com.jeanparedes.applogin.fullstack_app_login.DTO.RegisterRequestDTO;
import com.jeanparedes.applogin.fullstack_app_login.DTO.ResponseDTO;
import com.jeanparedes.applogin.fullstack_app_login.model.User;
import com.jeanparedes.applogin.fullstack_app_login.repository.UserRepository;
import com.jeanparedes.applogin.fullstack_app_login.service.TokenService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repository; // Repositorio para acceder a los datos de usuario
    private final PasswordEncoder passwordEncoder; // Codificador de contraseñas para encriptar y comparar contraseñas
    private final TokenService tokenService; // Servicio para manejar tokens JWT

    // Endpoint para el inicio de sesión
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO body){
        // Busca al usuario por su email en la base de datos
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));

        // Comprueba si la contraseña enviada coincide con la almacenada en la base de datos
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            // Genera un token JWT para el usuario autenticado
            String token = this.tokenService.generateToken(user);
            // Retorna una respuesta exitosa con el nombre de usuario y el token JWT
            return ResponseEntity.ok(new ResponseDTO(user.getUsername(), token));
        }
        // Retorna una respuesta de error en caso de credenciales inválidas
        return ResponseEntity.badRequest().build();
    }

    // Endpoint para el registro de nuevos usuarios
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterRequestDTO body){
        // Verifica si ya existe un usuario con el email proporcionado
        if(this.repository.findByEmail(body.email()).isEmpty()) {
            // Crea un nuevo usuario con los datos proporcionados
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password())); // Encripta la contraseña
            newUser.setEmail(body.email());
            newUser.setUsername(body.name());
            this.repository.save(newUser); // Guarda el nuevo usuario en la base de datos

            // Genera un token JWT para el nuevo usuario registrado
            String token = this.tokenService.generateToken(newUser);
            // Retorna una respuesta exitosa con el nombre de usuario y el token JWT
            return ResponseEntity.ok(new ResponseDTO(newUser.getUsername(), token));
        }
        // Retorna una respuesta de error si ya existe un usuario con ese email
        return ResponseEntity.badRequest().build();
    }
}
