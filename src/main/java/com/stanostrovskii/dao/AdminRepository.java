package com.stanostrovskii.dao;

import org.springframework.data.repository.CrudRepository;

import com.stanostrovskii.model.Admin;

public interface AdminRepository extends CrudRepository<Admin, Long> {
	Admin findByUsername(String username);
}
