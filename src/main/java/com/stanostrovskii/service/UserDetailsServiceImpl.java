package com.stanostrovskii.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stanostrovskii.dao.AdminRepository;
import com.stanostrovskii.dao.EmployeeRepository;
import com.stanostrovskii.model.Admin;
import com.stanostrovskii.model.Employee;

/**
 * Due to the need to fetch users from two independent repositories, this service is a
 * little more involved. First it attempts to find the user among admins, then among employees.
 * If not found in either, a UsernameNotFoundException is thrown.
 * 
 * @author Stan
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// first looks whether the username belongs to an admin
		Admin admin = adminRepository.findByUsername(username);
		if (admin != null) {
			return admin;
		}
		// if not found there, look in employee repository
		else {
			// recall that employee logins are their email address
			Employee employee = employeeRepository.findByEmail(username);
			if (employee != null)
				return employee;
		}
		throw new UsernameNotFoundException("User with username '" + username + "' not found");
	}

}
