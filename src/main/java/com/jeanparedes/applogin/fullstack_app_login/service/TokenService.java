package com.jeanparedes.applogin.fullstack_app_login.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jeanparedes.applogin.fullstack_app_login.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Obtiene la clave secreta desde la configuración de la aplicación (application.properties)
    @Value("${api.security.token.secret}")
    private String secret;

    // Método para generar un token JWT a partir de los datos del usuario
    public String generateToken(User user){
        try {
            // Configura el algoritmo de encriptación HMAC con la clave secreta
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Crea y firma el token JWT
            String token = JWT.create()
                    .withIssuer("login-auth-api") // Emisor del token
                    .withSubject(user.getEmail()) // Asunto del token (en este caso, el email del usuario)
                    .withExpiresAt(this.generateExpirationDate()) // Fecha de expiración del token
                    .sign(algorithm); // Firma el token con el algoritmo especificado

            return token;
        } catch (JWTCreationException exception){
            // Captura excepciones de creación del token JWT
            throw new RuntimeException("Error while authenticating");
        }
    }

    // Método para validar y verificar un token JWT
    public String validateToken(String token){
        try {
            // Configura el algoritmo de verificación con la clave secreta
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Verifica y decodifica el token JWT
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api") // Verifica el emisor del token
                    .build()
                    .verify(token) // Verifica el token JWT proporcionado
                    .getSubject(); // Devuelve el sujeto del token (en este caso, el email del usuario)
        } catch (JWTVerificationException exception) {
            // Captura excepciones de verificación del token JWT
            return null; // Devuelve null si hay un error de verificación
        }
    }

    // Método privado para generar la fecha de expiración del token (2 horas desde ahora)
    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
