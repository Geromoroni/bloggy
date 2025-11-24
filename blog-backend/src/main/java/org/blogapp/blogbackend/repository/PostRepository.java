package org.blogapp.blogbackend.repository;

import org.blogapp.blogbackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository  extends JpaRepository<Post, Long> {
    List<Post> findByAuthorId(Long authorId);

    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    List<Post> findAllByOrderByCreatedAtDesc();


}
