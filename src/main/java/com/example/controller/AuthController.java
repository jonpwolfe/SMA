package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.LoginRequest;
import com.example.model.LoginToken;
import com.example.model.User;
import com.example.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Register User Endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Validate and create user
            User registeredUser = userService.registerUser(loginRequest);

            // Return success response
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            // Return error response (e.g., if the username is taken)
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
     

    // Login User Endpoint (authentication)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Extract username and password (hashed in LoginToken)
        String username = loginRequest.getUsername();
        String rawPassword = loginRequest.getPassword();  // Extract password from LoginToken

        // Authenticate the user using rawPassword (password from the request body)
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, rawPassword)
        );

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Return success message or token if using JWT
        return ResponseEntity.ok("User logged in successfully");
    }
}


