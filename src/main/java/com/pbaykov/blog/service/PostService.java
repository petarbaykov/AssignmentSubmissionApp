package com.pbaykov.blog.service;

import com.pbaykov.blog.domain.Category;
import com.pbaykov.blog.domain.Post;
import com.pbaykov.blog.domain.User;
import com.pbaykov.blog.dto.PostRequest;
import com.pbaykov.blog.respository.CategoryRepository;
import com.pbaykov.blog.respository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Post createPost(PostRequest request, User user) throws Exception {
        Post post = new Post();
        Optional<Category> category = categoryRepository.findById(request.getCategory());

        if (category.isEmpty()) {
            throw new Exception("Category not found");
        }

        Category c = category.get();

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setCategory(c);
        post.setUser(user);
        postRepository.save(post);


        return post;
    }

    public Post updatePost(Long postId, PostRequest request) throws Exception {
        Optional<Post> post = postRepository.findById(postId);

        if (post.isEmpty()) {
            throw new Exception("Post not found");
        }

        Post p = post.get();

        Optional<Category> category = categoryRepository.findById(request.getCategory());

        if (category.isEmpty()) {
            throw new Exception("Category not found");
        }

        Category c = category.get();

        p.setTitle(request.getTitle());
        p.setContent(request.getContent());
        p.setCategory(c);
        postRepository.save(p);

        return p;
    }

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public Post findPostById(Long postId) throws Exception {
        Optional<Post> post = postRepository.findById(postId);

        if (post.isEmpty()) {
            throw new Exception("Post not found");
        }

        return post.get();
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
}
