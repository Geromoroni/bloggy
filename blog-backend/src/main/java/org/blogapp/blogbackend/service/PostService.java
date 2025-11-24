package org.blogapp.blogbackend.service;

import lombok.RequiredArgsConstructor;
import org.blogapp.blogbackend.model.Post;
import org.blogapp.blogbackend.model.User;
import org.blogapp.blogbackend.repository.PostRepository;
import org.blogapp.blogbackend.repository.UserRepository;
import org.blogapp.blogbackend.security.JwtService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    // ✅ Crear post autenticado
    public Post createPost(Post post, String token) {
        String jwt = token.substring(7); // eliminar "Bearer "
        String userEmail = jwtService.extractUsername(jwt);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        post.setAuthor(user);
        return postRepository.save(post);
    }

    // ✅ Obtener todos los posts (público)
    public List<Post> getAllPostsOrdered() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    // ✅ Obtener post por ID
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado con ID: " + id));
    }

    // ✅ Actualizar post (solo si es del autor)
    public Post updatePost(Long id, Post updatedPost, String token) {
        String jwt = token.substring(7);
        String userEmail = jwtService.extractUsername(jwt);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        if (!existingPost.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para editar este post");
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());

        return postRepository.save(existingPost);
    }

    // ✅ Eliminar post (solo si es del autor)
    public void deletePost(Long id, String token) {
        String jwt = token.substring(7);
        String userEmail = jwtService.extractUsername(jwt);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        if (!existingPost.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar este post");
        }

        postRepository.delete(existingPost);
    }

    // ✅ Obtener los posts de un usuario
    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByAuthorIdOrderByCreatedAtDesc(userId);
    }
}
