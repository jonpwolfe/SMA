package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import com.example.model.LoginRequest;

import com.example.util.JwtUtils;


@Service
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

   
    // Login method that returns ResponseEntity
    // Login method that generates JWT and returns an HTTP-only cookie
    public String loginUser(LoginRequest loginRequest) {
        try {
            // Log the attempt to authenticate
            logger.info("Attempting to authenticate user: {}", loginRequest.getUsername());

            // Authenticate the user with the provided credentials
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // Log successful authentication
            logger.info("Authentication successful for user: {}", loginRequest.getUsername());

            // Generate JWT token
            String jwtToken = jwtUtils.generateToken(authentication);
            logger.info("token = "+jwtToken);
            logger.info("JWT token generated for user: {}", loginRequest.getUsername());

            // Return the cookie
            return jwtToken;

        } catch (BadCredentialsException e) {
            logger.error("Invalid username or password for user: {}", loginRequest.getUsername());
            throw new RuntimeException("Invalid username or password", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during login for user: {}", loginRequest.getUsername(), e);
            throw new RuntimeException("Login failed", e);
        }
    }

}
