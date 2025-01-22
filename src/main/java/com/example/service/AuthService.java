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
    public void loginUser(LoginRequest loginRequest, HttpServletResponse response) {
        try {
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
            Cookie jwtCookie = createJwtCookie(jwtToken);
            response.addCookie(jwtCookie);

        } catch (Exception e) {
            // Handle authentication failure
            throw new RuntimeException("Invalid username or password: " + e.getMessage(), e);
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
