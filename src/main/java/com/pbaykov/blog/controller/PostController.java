package com.pbaykov.blog.controller;

import com.pbaykov.blog.domain.Post;
import com.pbaykov.blog.domain.User;
import com.pbaykov.blog.dto.PostRequest;
import com.pbaykov.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostService postService;


    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody PostRequest request, @AuthenticationPrincipal User user) {
        try {
            Post post = postService.createPost(request, user);

            return ResponseEntity.ok().body(post);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody PostRequest request, @AuthenticationPrincipal User user) {
        try {
            Post post = postService.updatePost(postId, request);

            // TODO: Check if editing post created by authenticated user
            return ResponseEntity.ok().body(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPosts() {
        List<Post> posts = postService.findAllPosts();

        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("{postId}")
    public ResponseEntity<?> findSinglePost(@PathVariable Long postId) {
        try {
            return ResponseEntity.ok().body(postService.findPostById(postId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        // TODO: check if authenticated user is author of the post

        postService.deletePost(postId);

        return ResponseEntity.ok().build();
    }
}
