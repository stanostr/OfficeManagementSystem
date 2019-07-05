package com.stanostrovskii.model;

public class LoginResponse {
	private String jwtToken;
	private String role;
	public LoginResponse(String jwtToken, String role) {
		this.jwtToken = jwtToken;
		this.role = role;
	}

	public String getToken() {
		return this.jwtToken;
	}

	public String getRole() {
		return role;
	}
}
