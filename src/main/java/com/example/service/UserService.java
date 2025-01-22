package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.LoginRequest;
import com.example.model.LoginToken;
import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Method to handle user registration logic
    public User registerUser(LoginRequest loginRequest) throws Exception {
        // Check if the username already exists
        if (userRepository.findByUsername(loginRequest.getUsername()).isPresent()) {
            throw new Exception("Username is already taken");
        }

        // Encode the password
        String encodedPassword = passwordEncoder.encode(loginRequest.getPassword());

        // Create a new User object
        User newUser = new User();
        newUser.setUsername(loginRequest.getUsername());
        newUser.setLoginToken(new LoginToken(encodedPassword)); // Set the encoded password

        // Save the user to the repository
        return userRepository.save(newUser);
    }
}
