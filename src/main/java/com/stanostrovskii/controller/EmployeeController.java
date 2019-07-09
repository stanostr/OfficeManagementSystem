package com.stanostrovskii.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stanostrovskii.dao.MeetingRoomRepository;
import com.stanostrovskii.dao.TaskRepository;
import com.stanostrovskii.dao.TrainingRoomRepository;
import com.stanostrovskii.model.Admin;
import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.Task;
import com.stanostrovskii.model.TaskForm;
import com.stanostrovskii.service.EmailService;
import com.stanostrovskii.util.EmployeeEmailUtil;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/employee")
@Api(tags = {"Employee"})
public class EmployeeController {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private EmailService emailService;

	@GetMapping("/account")
	public Employee getAccountDetails() {
		Employee thisEmployee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		thisEmployee.setPassword(null);
		return thisEmployee;
	}

	@GetMapping("/tasks")
	public List<TaskForm> getTasks() {
		Employee thisEmployee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Task> tasks = taskRepository.findByEmployee(thisEmployee);
		List<TaskForm> taskForms = new ArrayList<>();
		for (Task task : tasks) {
			taskForms.add(TaskForm.fromTask(task));
		}
		return taskForms;
	}

	@PutMapping("/tasks/{id}")
	public void completeTask(@PathVariable Long id) {
		Optional<Task> optional = taskRepository.findById(id);
		if (!optional.isPresent())
			return;
		Task task = optional.get();
		if(!task.isCompleted())
			EmployeeEmailUtil.sendCompletedTaskEmail(task, emailService);
		task.setCompleted(true);
		taskRepository.save(task);
	}
}