package com.stanostrovskii.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stanostrovskii.model.Admin;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(path = "/admin", produces = "application/json")
@Api(tags = { "Admin: Account"})
public class AdminAccountController {
	@GetMapping("/account")
	public Admin getAccountDetails() {
		Admin me = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		me.setPassword(null);
		return me;
	}
}
