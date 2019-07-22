package com.stanostrovskii.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stanostrovskii.RequestException;
import com.stanostrovskii.dao.DepartmentRepository;
import com.stanostrovskii.dao.EmployeeRepository;
import com.stanostrovskii.model.Department;
import com.stanostrovskii.model.Employee;

@Service
public class AdminEmployeeService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	public Employee getEmployeeById(Long id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		if (!optional.isPresent())
			throw new RequestException(HttpStatus.NOT_FOUND, "Employee not found.");
		return optional.get();
	}

	public List<Employee> getAllEmployees() {
		List<Employee> list = new ArrayList<>();
		employeeRepository.findAll().forEach(list::add);
		return list;
	}

	public Employee findEmployeeById(Long id) {
		Optional<Employee> optEmp = employeeRepository.findById(id);
		if (optEmp.isPresent()) {
			return optEmp.get();
		}
		throw new RequestException(HttpStatus.NOT_FOUND, "Employee not found.");
	}

	public Employee saveEmployee(Employee employee) {
		encodePass(employee);
		try {
			return employeeRepository.save(employee);
		} catch (Exception e) {
			throw new RequestException(HttpStatus.BAD_REQUEST, "Cannot violate unique constraints");
		}
	}

	public Employee updateEmployee(Employee patch) {
		Optional<Employee> optEmp = employeeRepository.findById(patch.getId());
		// only update if employee already exists
		if (optEmp.isPresent()) {
			Employee employee = optEmp.get();
			if (patch.getFirstName() != null) {
				employee.setFirstName(patch.getFirstName());
			}
			if (patch.getLastName() != null) {
				employee.setLastName(patch.getLastName());
			}
			if (patch.getContactNumber() != null) {
				employee.setContactNumber(patch.getContactNumber());
			}
			if (patch.getEmail() != null) {
				employee.setEmail(patch.getEmail());
			}
			if (patch.getDept() != null) {
				employee.setDept(patch.getDept());
			}
			if (patch.getPassword() != null) {
				employee.setPassword(patch.getPassword());
				encodePass(employee);
			}
			employee = employeeRepository.save(employee);
			if (patch.getPassword() != null)
				employee.setPassword(patch.getPassword());
			else
				employee.setPassword(null);
			return employee;
		}
		throw new RequestException(HttpStatus.NOT_FOUND, "Employee not found.");
	}

	private void encodePass(Employee employee) {
		employee.setPassword(passwordEncoder.encode(employee.getPassword()));
	}

	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
	}

	public List<Department> findAllDepartments() {
		List<Department> departments = new ArrayList<>();
		departmentRepository.findAll().forEach(departments::add);
		return departments;
	}

	public Department saveDepartment(Department department) {
		return departmentRepository.save(department);
	}

	public Department deleteDepartment(Long id) {
		Department dept = departmentRepository.findById(id).get();
		if(dept==null) throw new RequestException(HttpStatus.NOT_FOUND, "Department not found.");
		List<Employee> employeesWithDept = employeeRepository.findByDept(dept);
		for(Employee e: employeesWithDept)
		{
			e.setDept(null);
			employeeRepository.save(e);
		}
		departmentRepository.deleteById(id);
		return dept;
	}

	public Department updateDepartment(Department department) {
		return departmentRepository.save(department);
	}
}
