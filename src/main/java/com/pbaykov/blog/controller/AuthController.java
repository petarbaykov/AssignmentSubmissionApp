package com.pbaykov.blog.controller;

import ch.qos.logback.core.util.Duration;
import com.pbaykov.blog.domain.User;
import com.pbaykov.blog.dto.LoginCredentialsRequest;
import com.pbaykov.blog.dto.UserDTO;
import com.pbaykov.blog.service.UserService;
import com.pbaykov.blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Value("${cookies.domain}")
    private String domain;

    @PostMapping("login")
    public ResponseEntity<?> login (@RequestBody LoginCredentialsRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            User user = (User) authenticate.getPrincipal();
            user.setPassword(null);

            String token = jwtUtil.generateToken(user);
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                    .domain(domain)
                    .path("/")
                    .httpOnly(true)
                    .maxAge(Duration.buildByDays(30).getMilliseconds())
                    .build();

            ResponseCookie authCookie = ResponseCookie.from("authenticated", "1")
                    .domain(domain)
                    .path("/")
                    .maxAge(Duration.buildByDays(30).getMilliseconds())
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                    .body(token);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("signup")
    public ResponseEntity<?> signup (@RequestBody UserDTO request) {
        try {
            User user = userService.createUser(request);

            // TODO
            // 1. Validate email
            // 2. Validate username exists
            // 3. Validate email exists
            // 4. Validate password length

            String token = jwtUtil.generateToken(user);
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                    .domain(domain)
                    .path("/")
                    .httpOnly(true)
                    .maxAge(Duration.buildByDays(30).getMilliseconds())
                    .build();

            ResponseCookie authCookie = ResponseCookie.from("authenticated", "1")
                    .domain(domain)
                    .path("/")
                    .maxAge(Duration.buildByDays(30).getMilliseconds())
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                    .body(token);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
