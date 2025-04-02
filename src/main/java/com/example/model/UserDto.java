package com.example.model;

public class UserDto {
	
	private String username;
	
	private String name;
	
	public UserDto(String username) {
		this.setUsername(username);
	}

	public UserDto(User user) {
		this.setUsername(user.getUsername());
		this.setName(user.getName());
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
