package com.example.controller;

import java.util.HashMap;
import java.util.Map;

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

  
    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            // Call the AuthService to authenticate and generate the JWT token as a cookie
            String jwtToken = authService.loginUser(loginRequest);
                
            response.addHeader("Authorization", "Bearer " + jwtToken);

            // Prepare a JSON response
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "User logged in successfully");

            // Return the response as JSON
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            // Return 401 Unauthorized if login fails with a proper JSON error message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Login failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}
