package com.stanostrovskii.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.stanostrovskii.dao.TaskRepository;
import com.stanostrovskii.model.Admin;
import com.stanostrovskii.model.Department;
import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.Task;
import com.stanostrovskii.model.TaskForm;
import com.stanostrovskii.service.EmailService;
import com.stanostrovskii.util.EmployeeEmailUtil;

import io.swagger.annotations.Api;


@RestController
@RequestMapping(path = "/admin", produces = "application/json")
@Api(tags = { "Admin: Employee and Dept. Management" })
public class AdminEmployeeController {

	private static final Logger log = LoggerFactory.getLogger(AdminEmployeeController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private EmailService emailService;

	@GetMapping(value = "/employees")
	public List<Employee> viewAllEmployees() {
		List<Employee> list = new ArrayList<>();
		employeeRepository.findAll().forEach(list::add);
		list.replaceAll(e -> {
			e.setPassword(null);
			return e;
		});
		return list;
	}

	@GetMapping(value = "/employees/{id}")
	public ResponseEntity<Employee> viewEmployeeById(@PathVariable String id) {
		Optional<Employee> optEmp = employeeRepository.findById(Long.parseLong(id));
		if (optEmp.isPresent()) {
			Employee employee = optEmp.get();
			employee.setPassword(null); // to avoid transmitting encrypted password
			return new ResponseEntity<>(employee, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@PostMapping(value = "/employees")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee saveEmployee(@RequestBody Employee employee) {
		String plaintextPass = employee.getPassword();
		encodePass(employee);
		employee = employeeRepository.save(employee);
		employee.setPassword(plaintextPass); // to avoid sending encoded pass
		EmployeeEmailUtil.sendNewEmployeeEmail(employee, emailService);
		return employee;
	}

	@PutMapping(value = "/employees")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee patch) {
		Optional<Employee> optEmp = employeeRepository.findById(patch.getId());
		// only update if employee already exists
		if (optEmp.isPresent()) {
			Employee employee = optEmp.get();
			if (patch.getFirstName() != null) {
				employee.setFirstName(employee.getFirstName());
			}
			if (patch.getLastName() != null) {
				employee.setLastName(employee.getLastName());
			}
			if (patch.getContactNumber() != null) {
				employee.setContactNumber(patch.getContactNumber());
			}
			if (patch.getEmail() != null) {
				employee.setEmail(patch.getEmail());
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
			return new ResponseEntity<>(employee, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@DeleteMapping(value = "/employees")
	public void deleteEmployee(@RequestBody Employee employee) {
		employeeRepository.delete(employee);
	}

	@GetMapping(value = "/departments")
	public List<Department> viewDepartments() {
		List<Department> departments = new ArrayList<>();
		departmentRepository.findAll().forEach(departments::add);
		return departments;
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

	private void encodePass(Employee employee) {
		employee.setPassword(passwordEncoder.encode(employee.getPassword()));
	}
}
