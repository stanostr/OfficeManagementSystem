package com.stanostrovskii.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.stanostrovskii.dao.DepartmentRepository;
import com.stanostrovskii.dao.EmployeeRepository;
import com.stanostrovskii.model.Department;
import com.stanostrovskii.model.Employee;

/*TODO I want to split this Controller into two controllers. 
 * One for methods pertaining to employees. 
 * One for methods pertaining to departments.
*/
@RestController
@RequestMapping(produces = "application/json")
public class AdminController {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@GetMapping(value = "/employees")
	public List<Employee> viewAllEmployees() {
		// convert the iterable to list
		List<Employee> list = new ArrayList<>();
		employeeRepository.findAll().forEach(list::add);
		return list;
	}

	@GetMapping(value = "/employees/{id}")
	public ResponseEntity<Employee> viewEmployeeById(@PathVariable String id) {
		Optional<Employee> optEmp = employeeRepository.findById(Long.parseLong(id));
		if (optEmp.isPresent()) {
			return new ResponseEntity<>(optEmp.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@PostMapping(value = "/employees")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee saveEmployee(@RequestBody Employee employee) {
		return employeeRepository.save(employee);
	}

	@PutMapping(value = "/employees")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
		Optional<Employee> optEmp = employeeRepository.findById(employee.getId());
		// only update if employee already exists
		if (optEmp.isPresent()) {
			Employee updatedEmployee = employeeRepository.save(employee);
			return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@DeleteMapping(value = "/employees")
	public void deleteEmployee(@RequestBody Employee employee) {
		employeeRepository.delete(employee);
	}

	@PostMapping(value = "/departments")
	@ResponseStatus(HttpStatus.CREATED)
	public Department addDepartment(@RequestBody Department department) {
		return departmentRepository.save(department);
	}

	@DeleteMapping(value = "/departments")
	public void deleteDepartment(@RequestBody Department department) {
		departmentRepository.delete(department);
	}

	@PutMapping(value = "/departments")
	public Department updateDepartment(@RequestBody Department department) {
		return departmentRepository.save(department);
	}
}
