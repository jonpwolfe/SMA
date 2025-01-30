package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.service.CustomUserDetailsService;

@RestController
@RequestMapping("/user")
public class UserController {

    private CustomUserDetailsService userService; // Assume you have a service to fetch user data

    public UserController(CustomUserDetailsService userService) {
        this.userService = userService;
    }

    @GetMapping(produces="application/json")
    public ResponseEntity<User> getUserByCookie(@CookieValue(name = "authToken", required = false) String authToken) {
        try {
            if (authToken == null || authToken.isEmpty()) {
                return ResponseEntity.status(401).body(null); // Unauthorized if no cookie is provided
            }
           System.out.println(authToken);
            // Validate and fetch user based on the token
            User user = userService.getUserByAuthToken(authToken);
            if (user == null) {
                return ResponseEntity.status(404).body(null); // User not found
            }

            return ResponseEntity.ok(user); // Return the user object
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Handle unexpected server errors
        }
    }
}
