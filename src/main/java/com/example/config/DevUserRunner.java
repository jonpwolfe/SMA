package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.model.LoginRequest;
import com.example.service.RegistrationService;


@Component
@Profile("dev")
public class DevUserRunner implements ApplicationRunner {

    @Autowired
    private RegistrationService registrationService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
    		LoginRequest loginRequest = new LoginRequest("jon", "123");
        	registrationService.registerUser(loginRequest);
            System.out.println("Development user created: jon");
        }
    }

