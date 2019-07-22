package com.stanostrovskii.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.stanostrovskii.model.Department;
import com.stanostrovskii.model.Employee;
import com.stanostrovskii.service.AdminEmployeeService;
import com.stanostrovskii.service.EmailService;
import com.stanostrovskii.util.EmployeeEmailUtil;

import io.swagger.annotations.Api;
import sun.util.logging.resources.logging;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/admin", produces = "application/json")
@Api(tags = { "Admin: Employee and Dept. Management" })
public class AdminEmployeeController {

	@Autowired
	private AdminEmployeeService employeeService;

	@Autowired
	private EmailService emailService;

	@GetMapping(value = "/employees")
	public List<Employee> viewAllEmployees() {
		List<Employee> list =  employeeService.getAllEmployees();
		//set all passwords to null to prevent transmitting encrypted passwords,
		//which are of no benefit to a legitimate consumer
		list.replaceAll(e -> {
			e.setPassword(null);
			return e;
		});
		return list;
	}

	@GetMapping(value = "/employees/{id}")
	public ResponseEntity<Employee> viewEmployeeById(@PathVariable Long id) {
		Employee employee = employeeService.findEmployeeById(id);
		employee.setPassword(null);
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}

	@PostMapping(value = "/employees")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee saveEmployee(@RequestBody Employee employee) {
		String plaintextPass = employee.getPassword();
		employee = employeeService.saveEmployee(employee);
		employee.setPassword(plaintextPass); // to avoid sending encoded pass
		EmployeeEmailUtil.sendNewEmployeeEmail(employee, emailService);
		return employee;
	}

	@PutMapping(value = "/employees")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee patch) {
		Employee employee = employeeService.updateEmployee(patch);
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}

	@DeleteMapping(value = "/employees/{id}")
	public void deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
	}

	@GetMapping(value = "/departments")
	public List<Department> viewDepartments() {
		return employeeService.findAllDepartments();
	}

	@PostMapping(value = "/departments")
	@ResponseStatus(HttpStatus.CREATED)
	public Department addDepartment(@RequestBody Department department) {
		return employeeService.saveDepartment(department);
	}

	@DeleteMapping(value = "/departments/{id}")
	public Department deleteDepartment(@PathVariable Long id) {
		return employeeService.deleteDepartment(id);
	}

	@PutMapping(value = "/departments")
	public Department updateDepartment(@RequestBody Department department) {
		return employeeService.updateDepartment(department);
	}

}
