package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.model.CustomUserDetails;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.util.JwtUtils;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Log the attempt to load a user by username
        logger.info("Attempting to load user by username: {}", username);

        // Retrieve the user from the repository as an Optional
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // Check if user is found
        if (!optionalUser.isPresent()) {
            // Log the error for better tracking
            logger.error("User with username '{}' not found", username);
            throw new UsernameNotFoundException("User with username '" + username + "' not found");
        }

        // Return the custom user details object
        User user = optionalUser.get(); // Get the user from the Optional
        return new CustomUserDetails(user);
    }

    public boolean checkUsernameExists(String username) {
    // Check if username already exists
    	logger.info("Attempting to check if "+username+" exists");

    if (userRepository.findByUsername(username).isPresent()) {
    	logger.info(username+"exists");

        return true;
    }else
    	logger.info(username+"does not exist");
    	return false;
    }
    
    public void CreateUser(String username, String password, String name) {
        // Create a new user and encode the password
    	User user = new User();
    	user.setUsername(username);
    	user.setPassword(password);
    	user.setName(name);
    	// Log the event
    	logger.info("Attempting to save "+user.toString()+" to repository");
    	// Save user to the database
    	userRepository.save(user);
}
   public void CreateUser(User user) {
	    // Log the event
	   logger.info("Attempting to save "+user.toString()+" to repository");
	   // Save user to the database
	   userRepository.save(user);
   }

   public User getUserByAuthToken(String authToken) {
      String username = jwtUtils.getUsername(authToken);
       Optional<User> optionalUser = userRepository.findByUsername(username);
       if (!optionalUser.isPresent()) {
           // Log the error for better tracking
           logger.error("User with username '{}' not found", username);
           throw new UsernameNotFoundException("User with username '" + username + "' not found");
       }

       // Return the custom user details object
       return optionalUser.get(); // return the user from the Optional
       
       
   }
}
