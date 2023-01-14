package com.pbaykov.AssignmentSubmissionApp.service;

import com.pbaykov.AssignmentSubmissionApp.domain.Authority;
import com.pbaykov.AssignmentSubmissionApp.domain.User;
import com.pbaykov.AssignmentSubmissionApp.dto.UserDTO;
import com.pbaykov.AssignmentSubmissionApp.respository.AuthorityRepository;
import com.pbaykov.AssignmentSubmissionApp.respository.UserRepository;
import com.pbaykov.AssignmentSubmissionApp.util.CustomPasswordEncoder;
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
