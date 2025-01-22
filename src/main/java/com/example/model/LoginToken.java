package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Entity
public class LoginToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
    private String hashedPassword;
	
	@Column(nullable = false)
    private String salt;

    // Default constructor for JPA
    public LoginToken() {}

    // Constructor to initialize with a raw password
    public LoginToken(String rawPassword) {
        this.salt = generateSalt();
        this.hashedPassword = hashPassword(rawPassword, this.salt);
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
    private void setHashedPassword(String hashedPassword) {
    	this.hashedPassword = hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    // Verify the password
    public boolean verifyPassword(String rawPassword) {
        String hashedInput = hashPassword(rawPassword, this.salt);
        return hashedPassword.equals(hashedInput);
    }

    // Generate a random salt
    private String generateSalt() {
        byte[] saltBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    // Hash the password with the salt
    private String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPassword = salt + password;
            byte[] hashBytes = digest.digest(saltedPassword.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    public void setPassword(String password) {
    	this.setHashedPassword(this.hashPassword(password, this.getSalt()));
    	
    }
}
