package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.RegistrationRequest;

@Service
public class RegistrationService {
	
	private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	 
	 // Register a new user
    public void registerUser(RegistrationRequest registrationRequest) {
    	//check if username exists
    	userDetailsService.checkUsernameExists(registrationRequest.getUsername());
    	
        // Create a new user and encode the password
        userDetailsService.CreateUser(registrationRequest.getUsername(), passwordEncoder.encode(registrationRequest.getPassword()),registrationRequest.getName());;
        
        //Log successful registration
        logger.info("Registered user: '{}", registrationRequest.getUsername(), "' with name: {}",registrationRequest.getName());
    }
}
