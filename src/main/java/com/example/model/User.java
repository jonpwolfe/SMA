package com.example.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @OneToOne(cascade = CascadeType.ALL)  // One-to-one relationship, cascade all operations (e.g., save, delete)
    @JoinColumn(name = "login_token_id")  // Foreign key to the LoginToken table
    private LoginToken loginToken;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LoginToken getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(LoginToken loginToken) {
        this.loginToken = loginToken;
    }

    public List<String> getRoles() {
        return new ArrayList<>(roles); // Return a copy for immutability
    }

    public void setRoles(List<String> roles) {
        this.roles = new ArrayList<>(roles); // Use a copy for immutability
    }
}
