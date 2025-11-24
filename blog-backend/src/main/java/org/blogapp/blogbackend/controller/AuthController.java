    package org.blogapp.blogbackend.controller;

    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.blogapp.blogbackend.dto.RegisterRequest;
    import org.blogapp.blogbackend.dto.UserResponseDTO;
    import org.blogapp.blogbackend.model.User;
    import org.blogapp.blogbackend.repository.UserRepository;
    import org.blogapp.blogbackend.security.JwtService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.web.bind.annotation.*;
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.responses.ApiResponse;
    import io.swagger.v3.oas.annotations.responses.ApiResponses;
    import io.swagger.v3.oas.annotations.tags.Tag;


    import java.util.Map;

    @RestController
    @RequestMapping("/api/auth")
    @RequiredArgsConstructor
    @Tag(name = "Autenticación", description = "Registro, login y obtención del usuario actual")
    public class AuthController {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;

        @Operation(summary = "Registrar un nuevo usuario")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
                @ApiResponse(responseCode = "400", description = "Email ya registrado")
        })

        @PostMapping("/register")
        public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "El email ya está en uso"));
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                    "message", "Usuario registrado correctamente",
                    "email", user.getEmail()
            ));
        }



        @Operation(summary = "Iniciar sesión y obtener token JWT")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
                @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
        })
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody Map<String, String> creds) {
            try {
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(creds.get("email"), creds.get("password"))
                );

                // Obtener usuario desde DB
                User user = userRepository.findByEmail(creds.get("email"))
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // Claims completos que el frontend necesita
                Map<String, Object> claims = Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "roles", "ROLE_USER"
                );

                // Generar token ahora con CLAIMS completos
                String token = jwtService.generateToken(user.getEmail(), claims);

                return ResponseEntity.ok(
                        Map.of(
                                "token", token,
                                "user", new UserResponseDTO(
                                        user.getId(),
                                        user.getUsername(),
                                        user.getEmail()
                                )
                        )
                );

            } catch (AuthenticationException ex) {
                return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
            }
        }


        @Operation(summary = "Obtener el usuario autenticado actual")
        @ApiResponse(responseCode = "200", description = "Datos del usuario actual")
        @GetMapping("/me")
        public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
            String jwt = token.substring(7);
            String userEmail = jwtService.extractUsername(jwt);

            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 👇 devolvemos usando el DTO
            return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail()));
        }



    }
