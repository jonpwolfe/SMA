package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.model.CustomUserDetails;
import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Log the attempt to load a user by username
        logger.info("Attempting to load user by username: {}", username);
        
        // Retrieve the user from the repository and handle the case when the user doesn't exist
        User user = userRepository.findByUsername(username).orElse(null);
        
        if (user == null) {
            // Log the error for better tracking and throw a more descriptive exception
            logger.error("User with username '{}' not found", username);
            throw new UsernameNotFoundException("User with username '" + username + "' not found");
        }

        // Return the custom user details object
        return new CustomUserDetails(user);
    }
}
