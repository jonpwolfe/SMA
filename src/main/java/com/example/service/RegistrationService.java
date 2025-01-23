package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.LoginRequest;

@Service
public class RegistrationService {
	
	private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	 
	 // Register a new user
    public void registerUser(LoginRequest loginRequest) {
    	//check if username exists
    	userDetailsService.checkUsernameExists(loginRequest);
    	
        // Create a new user and encode the password
        userDetailsService.CreateUser(loginRequest.getUsername(), passwordEncoder.encode(loginRequest.getPassword()));;
        
        //Log successful registration
        logger.info("Registered user: {}", loginRequest.getUsername());
    }
}
