package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.LoginRequest;
import com.example.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true") // Enable credentials
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody LoginRequest loginRequest) {
        try {
            authService.registerUser(loginRequest);
            return ResponseEntity.status(201).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
;

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            // Call the AuthService to authenticate and generate the JWT token as a cookie
            Cookie jwtCookie = authService.loginUser(loginRequest);

            // Add the JWT cookie to the response
            response.addCookie(jwtCookie);

            // Return a success response with 200 OK status
            return ResponseEntity.ok("User logged in successfully");
        } catch (Exception e) {
            // Return 401 Unauthorized if login fails
            return ResponseEntity.status(401).body("Login failed: " + e.getMessage());
        }
    }
}
