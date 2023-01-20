package com.pbaykov.blog.respository;


import com.pbaykov.blog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Post> findByUserIdAndCategoryIdOrderByCreatedAtDesc(Long userId, Long categoryId);
}
