package com.stanostrovskii.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthorizationFilter jwtAuthorizationFilter;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
						"Unauthorized");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO make crsf work.
		http.csrf().disable()
				// requests to these paths require authentication
				.authorizeRequests().antMatchers("/employees", "/departments").authenticated()
				// requests to these paths require admin privileges
				.antMatchers("/employees", "/departments").access("hasRole('ROLE_ADMIN')").antMatchers("/").permitAll()
				.and()
				// session won't be used to store user's state.
				.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Add a filter to validate the tokens with every request
		http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
