package com.pbaykov.blog.respository;


import com.pbaykov.blog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
