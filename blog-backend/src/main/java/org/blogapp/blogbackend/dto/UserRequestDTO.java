package org.blogapp.blogbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO (
    @NotBlank(message = "El nombre de usuario es obligatorio")
    String username,
    @Email(message =  "Debe ingresar un mail valido")
    String Email,
    @NotBlank(message = "La contraseña no puede estar vacia")
    String password
){}

