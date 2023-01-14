package com.pbaykov.AssignmentSubmissionApp.web;

import ch.qos.logback.core.util.Duration;
import com.pbaykov.AssignmentSubmissionApp.domain.Authority;
import com.pbaykov.AssignmentSubmissionApp.domain.User;
import com.pbaykov.AssignmentSubmissionApp.dto.LoginCredentialsRequest;
import com.pbaykov.AssignmentSubmissionApp.dto.UserDTO;
import com.pbaykov.AssignmentSubmissionApp.respository.AuthorityRepository;
import com.pbaykov.AssignmentSubmissionApp.respository.UserRepository;
import com.pbaykov.AssignmentSubmissionApp.service.UserService;
import com.pbaykov.AssignmentSubmissionApp.util.CustomPasswordEncoder;
import com.pbaykov.AssignmentSubmissionApp.util.JwtUtil;
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

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
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
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .domain(domain)
                    .path("/")
                    .maxAge(Duration.buildByDays(30).getMilliseconds())
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(token);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("signup")
    public ResponseEntity<?> signup (@RequestBody UserDTO request) {
        try {
            User user = userService.createUser(request);

            String token = jwtUtil.generateToken(user);
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .domain(domain)
                    .path("/")
                    .maxAge(Duration.buildByDays(30).getMilliseconds())
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(token);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
