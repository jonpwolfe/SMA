package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.RegistrationRequest;
import com.example.service.RegistrationService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true") // Enable credentials
@RequestMapping("/")
public class RegistrationController {
	
	@Autowired
	RegistrationService registrationService;
	
	  @PostMapping(value ="register", consumes = "application/json", produces = "application/json")
	    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {
	        try {
	            registrationService.registerUser(registrationRequest);
	            return ResponseEntity.status(201).body("User registered successfully");
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
	        }
	    }

}
