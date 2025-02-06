package com.example.util;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.Cookie;

@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private  JwtProperties jwtProperties;
    private static Key SIGNING_KEY;
    public JwtUtils(JwtProperties jwtProperties) {
    	this.jwtProperties = jwtProperties;
    	JwtUtils.SIGNING_KEY =Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(SIGNING_KEY)  // Use the strong key
                .compact();
    }

    // Create a JWT Cookie
    public Cookie createJwtCookie(String token) {
        Cookie cookie = new Cookie(jwtProperties.getCookieName(), token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Use secure cookies in production
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtProperties.getExpiration() / 1000)); // Set cookie expiration
        return cookie;
    }
 // Validate a JWT token
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            logger.error("JWT token is null or empty");
            return false;
        }

        try {
            Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token: {}", token, e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", token, e);
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT token: {}", token, e);
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", token, e);
        }

        return false;
    }

    // Extract username from the JWT token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
