package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.model.UserDto;
import com.example.service.CustomUserDetailsService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private CustomUserDetailsService userService; // Assume you have a service to fetch user data

    public UserController(CustomUserDetailsService userService) {
        this.userService = userService;
    }
    
    @GetMapping(produces = "application/json")
    public ResponseEntity<UserDto> getUserByToken(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        try {
            // Log the incoming Authorization header
            logger.info("Received Authorization header: {}", authorizationHeader);

            // Check if the Authorization header is missing or improperly formatted
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                logger.warn("Invalid or missing Authorization header.");
                return ResponseEntity.status(401).body(null); // Unauthorized
            }

            // Extract the JWT token (remove "Bearer " prefix)
            String authToken = authorizationHeader.substring(7);
            
            // Validate and fetch user based on the token
            User user = userService.getUserByAuthToken(authToken);
            if (user == null) {
                logger.error("User not found for authToken: {}", authToken);
                return ResponseEntity.status(404).body(null); // User not found
            }

            logger.info("User found: {}", user.getUsername());
            UserDto userDto = new UserDto(user);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            logger.error("Error while processing Authorization token", e);
            return ResponseEntity.status(500).body(null); // Internal server error
        }
    }


}
