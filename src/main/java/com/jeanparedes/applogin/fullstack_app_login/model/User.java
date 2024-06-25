package com.jeanparedes.applogin.fullstack_app_login.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok: Genera automáticamente getters, setters, toString, equals, hashCode
@AllArgsConstructor // Lombok: Crea un constructor con todos los argumentos
@NoArgsConstructor // Lombok: Crea un constructor vacío
@Entity // JPA: Indica que esta clase es una entidad que se mapea a una tabla en la base de datos
@Table(name = "users") // JPA: Especifica el nombre de la tabla en la base de datos
public class User {

    @Id // JPA: Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: Generación automática de valores para la clave primaria
    private Long id;

    @Column(name = "username") // JPA: Mapea este campo a una columna en la tabla con nombre 'username'
    private String username;

    @Column(name = "password") // JPA: Mapea este campo a una columna en la tabla con nombre 'password'
    private String password;

    @Column(name = "email") // JPA: Mapea este campo a una columna en la tabla con nombre 'email'
    private String email;

    @Column(name = "role") // JPA: Mapea este campo a una columna en la tabla con nombre 'role'
    private String role = "User"; // Valor predeterminado para el rol
}
