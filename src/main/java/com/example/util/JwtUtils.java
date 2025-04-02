package com.example.util;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.config.JwtProperties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private JwtProperties jwtProperties;
    private Key signingKey; // Non-static signing key

    // Constructor initializes signingKey from JwtProperties
    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    // Generate JWT token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName(); // Get username from Authentication object
        long expirationTime = jwtProperties.getExpiration(); // Fetch expiration time from properties

        // Optionally, you can include authorities (roles) in the token
        String authorities = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username) // Set the username as the subject of the token
                .claim("roles", authorities) // Optional: Add roles to the token
                .setIssuedAt(new Date()) // Set the current time as issued time
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Set the expiration time
                .signWith(signingKey, SignatureAlgorithm.HS256) // Sign the token using HS256 and the signing key
                .compact();
    }


    // Extract the username (subject) from the JWT token
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey) // Set the signing key to validate the token
                .build()
                .parseClaimsJws(token) // Parse the token
                .getBody()
                .getSubject(); // Extract the subject (username)
    }

    // Validate if the token is valid by comparing the username and expiration time
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = getUsername(token); // Extract the username from the token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Ensure the username matches and the token is not expired
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Check if the expiration date is before the current date
    }

    // Extract the expiration date from the token
    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey) // Set the signing key to validate the token
                .build()
                .parseClaimsJws(token) // Parse the JWT token
                .getBody()
                .getExpiration(); // Get the expiration date from the token body
    }

    // Simple check to see if the token is expired or invalid
    public boolean isTokenExpiredOrInvalid(String token) {
        try {
            return isTokenExpired(token); // Check if the token is expired
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            // Invalid or malformed token
            logger.error("Invalid JWT Token", e);
            return true; // If any exception occurs, consider the token invalid
        } catch (IllegalArgumentException e) {
            // Token is empty or not provided
            logger.error("JWT Token is empty or null", e);
            return true;
        }
    }
}
