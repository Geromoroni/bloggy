package org.blogapp.blogbackend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.blogapp.blogbackend.dto.UserResponseDTO;
import org.blogapp.blogbackend.model.User;
import org.blogapp.blogbackend.security.JwtService;
import org.blogapp.blogbackend.service.FileStorageService;
import org.blogapp.blogbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final JwtService jwtService;


    @PostMapping //POST /api/users
    public ResponseEntity<User>createUser(@RequestBody User user){
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }


    @GetMapping //GET /api/users
    public ResponseEntity<List<User>>getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        System.out.println("Buscando usuario con ID: " + id);
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload-profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
        String userEmail = jwtService.extractUsername(token.substring(7));
        String fileUrl = fileStorageService.store(file);
        User updatedUser = userService.updateProfileImage(userEmail, fileUrl);

        return ResponseEntity.ok(new UserResponseDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail()));
    }

    @PostMapping("/upload-banner-picture")
    public ResponseEntity<?> uploadBannerPicture(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
        String userEmail = jwtService.extractUsername(token.substring(7));
        String fileUrl = fileStorageService.store(file);
        User updatedUser = userService.updateBannerImage(userEmail, fileUrl);

        return ResponseEntity.ok(Map.of("bannerUrl", updatedUser.getBannerImageUrl()));
    }
}
