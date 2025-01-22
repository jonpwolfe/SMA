package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.LoginRequest;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.util.JwtUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    // Register a new user
    public void registerUser(LoginRequest loginRequest) {
        // Check if username already exists
        if (userRepository.findByUsername(loginRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        // Create a new user and encode the password
        User user = new User();
        user.setUsername(loginRequest.getUsername());
        user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));

        // Save user to the database
        userRepository.save(user);
    }

    // Authenticate user and set JWT cookie
    public void loginUser(LoginRequest loginRequest, HttpServletResponse response) {
        // Authenticate user using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        // Generate JWT token
        String jwtToken = jwtUtils.generateToken(authentication);

        // Add JWT token to response as an HttpOnly cookie
        Cookie jwtCookie = jwtUtils.createJwtCookie(jwtToken);
        response.addCookie(jwtCookie);
    }
}
