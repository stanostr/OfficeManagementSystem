package com.stanostrovskii.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.stanostrovskii.model.Department;

public interface DepartmentRepository extends CrudRepository<Department, Long>{
	Optional<Department> findByDepartmentName(String name);
}
