package com.pbaykov.blog.controller;

import com.pbaykov.blog.domain.Post;
import com.pbaykov.blog.domain.User;
import com.pbaykov.blog.dto.PostRequest;
import com.pbaykov.blog.service.FileStorageService;
import com.pbaykov.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") Long category,
            @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal User user
    ) {
        try {
            String imageFilename = fileStorageService.storeFile(image);
            Post post = postService.createPost(new PostRequest(title, content, category, imageFilename), user);

            return ResponseEntity.ok().body(post);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value = "{postId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updatePost(
            @PathVariable Long postId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") Long category,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            String imageFilename = null;
            if (image != null) {
                imageFilename = fileStorageService.storeFile(image);
            }
            
            Post post = postService.updatePost(postId, new PostRequest(title, content, category, imageFilename));

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

    @GetMapping("/users")
    public ResponseEntity<?> getAllUserPosts(@AuthenticationPrincipal User user) {
        System.out.println(user.getId());
        List<Post> posts = postService.findAllPostsForUser(user.getId());

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
