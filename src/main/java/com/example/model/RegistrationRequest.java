package com.example.model;

public class RegistrationRequest {
	private String name;
	private String username;
	private String password;  // Plain-text password submitted by the user

	public RegistrationRequest(String username, String password, String name) {
	    this.username = username;
	    this.password = password;
	    this.name = name;
	}
	
	// Getters and Setters
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
	    this.username = username;
	}

	public String getPassword() {
	    return password;
	}

	public void setPassword(String password) {
	    this.password = password;
	}
	
}
