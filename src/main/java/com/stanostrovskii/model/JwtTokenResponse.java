package com.stanostrovskii.model;

import java.io.Serializable;

//TODO is it necessary to implement Serializable here?
public class JwtTokenResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String jwtToken;

	public JwtTokenResponse(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getToken() {
		return this.jwtToken;
	}
}
