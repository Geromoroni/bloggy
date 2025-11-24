package org.blogapp.blogbackend.dto;

public record UserResponseDTO(
        Long id,
        String username,
        String email
) {}