package com.stanostrovskii.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stanostrovskii.model.LoginRequest;
import com.stanostrovskii.model.LoginResponse;
import com.stanostrovskii.security.JwtTokenService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/login")
@Api(tags = { "Login" })
public class LoginController {
	
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenService jwtTokenUtil;
	@Autowired
	private UserDetailsService userDetailsService;

	@PostMapping
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest)
			throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		//TODO is there a way to make this prettier?
		String role = userDetails.getAuthorities().iterator().next().getAuthority();
		String token = jwtTokenUtil.generateToken(userDetails);
		log.info("Successfully authenticated user with username " + authenticationRequest.getUsername());
		return new ResponseEntity<LoginResponse>(new LoginResponse(token, role), HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("User disabled", e);
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid credentials", e);
		}
	}

}
