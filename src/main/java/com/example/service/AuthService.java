package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.example.model.LoginRequest;

import com.example.util.JwtUtils;

import jakarta.servlet.http.Cookie;

@Service
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
    @Autowired
    private CustomUserDetailsService userDetailsService;
   
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    // Register a new user
    public void registerUser(LoginRequest loginRequest) {
    	userDetailsService.checkUsernameExists(loginRequest);
        // Create a new user and encode the password
        userDetailsService.CreateUser(loginRequest.getUsername(), passwordEncoder.encode(loginRequest.getPassword()));;
    }
    // Login method that returns ResponseEntity
    // Login method that generates JWT and returns an HTTP-only cookie
    public Cookie loginUser(LoginRequest loginRequest) {
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
            logger.info("JWT token generated for user: {}", loginRequest.getUsername());

            // Create a cookie with the JWT token
            Cookie jwtCookie = createJwtCookie(jwtToken);
            logger.info("JWT cookie created for user: {}", loginRequest.getUsername());

            // Return the cookie
            return jwtCookie;

        } catch (BadCredentialsException e) {
            logger.error("Invalid username or password for user: {}", loginRequest.getUsername());
            throw new RuntimeException("Invalid username or password", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during login for user: {}", loginRequest.getUsername(), e);
            throw new RuntimeException("Login failed", e);
        }
    }

    /**
     * Creates an HttpOnly cookie for the JWT token.
     *
     * @param jwtToken The JWT token to store in the cookie.
     * @return A Cookie object configured with HttpOnly and Secure settings.
     */
    private Cookie createJwtCookie(String jwtToken) {
        Cookie jwtCookie = new Cookie("authToken", jwtToken);
        jwtCookie.setHttpOnly(true); // Prevent access via JavaScript
        jwtCookie.setSecure(false); // Use true in production to enforce HTTPS
        jwtCookie.setPath("/"); // Available for all endpoints
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // Set expiration (7 days)
        return jwtCookie;
    }

}
