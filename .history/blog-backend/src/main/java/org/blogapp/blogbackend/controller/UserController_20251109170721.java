package org.blogapp.blogbackend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.blogapp.blogbackend.model.User;
import org.blogapp.blogbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;


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
}
