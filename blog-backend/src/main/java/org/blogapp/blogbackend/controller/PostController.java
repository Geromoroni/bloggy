package org.blogapp.blogbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.blogapp.blogbackend.dto.PostResponseDTO;
import org.blogapp.blogbackend.model.Post;
import org.blogapp.blogbackend.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PostController {

    private final PostService postService;

    @Operation(summary = "Crear un nuevo post")
    @ApiResponse(responseCode = "200", description = "Post creado correctamente")
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody Post post, @RequestHeader("Authorization") String token) {
        Post savedPost = postService.createPost(post, token);
        PostResponseDTO dto = new PostResponseDTO(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCreatedAt(),
                savedPost.getAuthor().getId(),
                savedPost.getAuthor().getUsername()
        );
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Obtener todos los posts")
    @ApiResponse(responseCode = "200", description = "Lista de posts obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<Post> posts = postService.getAllPostsOrdered();

        List<PostResponseDTO> response = posts.stream()
                .map(p -> new PostResponseDTO(
                        p.getId(),
                        p.getTitle(),
                        p.getContent(),
                        p.getCreatedAt(),
                        p.getAuthor().getId(),
                        p.getAuthor().getUsername()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        Post p = postService.getPostById(id);
        PostResponseDTO dto = new PostResponseDTO(
                p.getId(),
                p.getTitle(),
                p.getContent(),
                p.getCreatedAt(),
                p.getAuthor().getId(),
                p.getAuthor().getUsername()
        );
        return ResponseEntity.ok(dto);

    }

        @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post, @RequestHeader("Authorization") String token) {
        Post updated = postService.updatePost(id, post, token);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        postService.deletePost(id, token);
        return ResponseEntity.ok(Map.of("message", "Post eliminado correctamente"));
    }

    @GetMapping("/my-posts")
    public ResponseEntity<List<Post>> getMyPosts(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(postService.getAllPostsOrdered());
    }
}
