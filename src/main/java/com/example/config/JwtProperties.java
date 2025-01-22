package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Component
@ConfigurationProperties(prefix = "jwt")
@Validated
public class JwtProperties {

	@NotNull(message = "JWT secret must not be null")
	private String secret;

	@Positive(message = "JWT expiration time must be positive")
	private long expiration;
	
	@NotNull(message = "JWT cookie name must not be null")
	private String cookieName;

    // Getters and Setters
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
