package com.stanostrovskii.controller;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stanostrovskii.model.Admin;
import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.Task;
import com.stanostrovskii.model.TaskForm;
import com.stanostrovskii.service.AdminTaskService;
import com.stanostrovskii.service.AdminEmployeeService;
import com.stanostrovskii.service.EmailService;
import com.stanostrovskii.util.EmployeeEmailUtil;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/admin", produces = "application/json")
@Api(tags = { "Admin: Task Management" })
public class AdminTaskController {

	private static final Logger log = LoggerFactory.getLogger(AdminTaskController.class);

	@Autowired
	private AdminEmployeeService employeeService;

	@Autowired
	private AdminTaskService taskService;
	
	@Autowired
	private EmailService emailService;

	@GetMapping("/employees/{id}/tasks")
	public List<TaskForm> getTasksByEmployeeId(@PathVariable Long id) {
		List<Task> tasks = taskService.findTasksByEmployeeId(id);
		List<TaskForm> taskForms = new ArrayList<>();
		for (Task task : tasks) {
			taskForms.add(TaskForm.fromTask(task));
		}
		return taskForms;
	}

	@PostMapping("/employees/{id}/tasks")
	public ResponseEntity<TaskForm> assignTask(@PathVariable Long id, @RequestBody TaskForm taskForm) {
		Employee employee = employeeService.getEmployeeById(id);
		Admin me = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (taskForm.getStartDate() == null)
			taskForm.setStartDate(new Date());
		Task task = taskService.saveTask(taskForm, employee, me);
		try {
			taskForm.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
		} catch (NullPointerException e) {}
		EmployeeEmailUtil.sendNewTaskEmail(employee, task, emailService);
		return new ResponseEntity<>(taskForm, HttpStatus.CREATED);
	}

	@DeleteMapping("/employees/{id}/tasks")
	public void deleteTasksByEmployee(@PathVariable String id) {
		taskService.deleteTasksByEmployeeId(id);
	}

	@DeleteMapping("/tasks/{id}")
	public TaskForm deleteTaskById(@PathVariable Long id) {
		return taskService.deleteTaskById(id);
	}

	@PutMapping("/tasks/{id}")
	public ResponseEntity<TaskForm> updateTask(@PathVariable Long id, @RequestBody TaskForm patch) {
		taskService.updateTask(id, patch);
		return new ResponseEntity<TaskForm>(patch, HttpStatus.OK);
	}

	@GetMapping("/tasks")
	public List<TaskForm> getTasksByAdmin() {
		Admin me = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Task> tasks = taskService.findByAdmin(me);
		List<TaskForm> taskForms = new ArrayList<>();
		for (Task task : tasks) {
			taskForms.add(TaskForm.fromTask(task));
		}
		return taskForms;
	}

}
