package com.stanostrovskii.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.stanostrovskii.model.Department;
import com.stanostrovskii.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
	Employee findByEmail(String email);
	List<Employee> findByDept(Department department);
}
