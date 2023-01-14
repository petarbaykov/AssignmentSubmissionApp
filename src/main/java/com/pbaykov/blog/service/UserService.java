package com.pbaykov.blog.service;

import com.pbaykov.blog.domain.Authority;
import com.pbaykov.blog.domain.User;
import com.pbaykov.blog.dto.UserDTO;
import com.pbaykov.blog.respository.AuthorityRepository;
import com.pbaykov.blog.respository.UserRepository;
import com.pbaykov.blog.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(customPasswordEncoder.getPasswordEncoder().encode(userDTO.getPassword()));
        user.setRegisteredAt(LocalDateTime.now());
        userRepository.save(user);

        Authority authority = new Authority("ROLE_USER");
        authority.setUser(user);
        authorityRepository.save(authority);

        return user;
    }
}
