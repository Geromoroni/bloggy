package org.blogapp.blogbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @Column(unique = true, nullable = false)
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @JsonIgnore // 👈 evita que se devuelva en respuestas JSON
    @Column(nullable = false)
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @JsonIgnore // 👈 evita recursión infinita (User -> Post -> User)
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "banner_image_url")
    private String bannerImageUrl;
}
