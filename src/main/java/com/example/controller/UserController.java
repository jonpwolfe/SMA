package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private CustomUserDetailsService userService; // Assume you have a service to fetch user data

    public UserController(CustomUserDetailsService userService) {
        this.userService = userService;
    }

    @GetMapping(produces="application/json")
    public ResponseEntity<User> getUserByCookie(@CookieValue(name = "authToken", required = false) String authToken) {
        try {
            // Log the incoming auth token
            logger.info("Received authToken: {}", authToken);

            if (authToken == null || authToken.isEmpty()) {
                logger.warn("Auth token is null or empty.");
                return ResponseEntity.status(401).body(null); // Unauthorized if no cookie is provided
            }

            // Validate and fetch user based on the token
            User user = userService.getUserByAuthToken(authToken);
            if (user == null) {
                logger.error("User not found for authToken: {}", authToken);
                return ResponseEntity.status(404).body(null); // User not found
            }

            logger.info("User found: {}", user.getUsername());
            return ResponseEntity.ok(user); // Return the user object
        } catch (Exception e) {
            logger.error("Error while processing authToken", e);
            return ResponseEntity.status(500).body(null); // Handle unexpected server errors
        }
    }
}
