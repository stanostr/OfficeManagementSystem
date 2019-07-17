package com.stanostrovskii.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

import com.stanostrovskii.RequestException;
import com.stanostrovskii.dao.EmployeeRepository;
import com.stanostrovskii.dao.TaskRepository;
import com.stanostrovskii.model.Admin;
import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.Task;
import com.stanostrovskii.model.TaskForm;
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
	private EmployeeRepository employeeRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private EmailService emailService;

	@GetMapping("/employees/{id}/tasks")
	public List<TaskForm> getTasksByEmployeeId(@PathVariable Long id) {
		List<Task> tasks = findTasksByEmployeeId(id);
		List<TaskForm> taskForms = new ArrayList<>();
		for (Task task : tasks) {
			taskForms.add(TaskForm.fromTask(task));
		}
		return taskForms;
	}

	@PostMapping("/employees/{id}/tasks")
	public ResponseEntity<TaskForm> assignTask(@PathVariable String id, @RequestBody TaskForm taskForm) {
		Optional<Employee> optional = employeeRepository.findById(Long.parseLong(id));
		if (!optional.isPresent())
			throw new RequestException(HttpStatus.NOT_FOUND, "Employee not found.");
		Employee employee = optional.get();
		Admin me = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (taskForm.getStartDate() == null)
			taskForm.setStartDate(new Date());

		Task task = taskForm.toTask();
		task.setEmployee(employee);
		task.setAdmin(me);
		task = taskRepository.save(task);
		EmployeeEmailUtil.sendNewTaskEmail(employee, task, emailService);
		return new ResponseEntity<>(taskForm, HttpStatus.CREATED);
	}

	@DeleteMapping("/employees/{id}/tasks")
	public void deleteTasksByEmployee(@PathVariable String id) {
		List<Task> tasks = findTasksByEmployeeId(Long.parseLong(id));
		taskRepository.deleteAll(tasks);
	}

	@DeleteMapping("/tasks/{id}")
	public TaskForm deleteTaskById(@PathVariable Long id) {
		try {
			TaskForm deletedForm = TaskForm.fromTask(taskRepository.findById(id).get());
			taskRepository.deleteById(id);
			return deletedForm;
		} catch (EmptyResultDataAccessException | NullPointerException e) {
			log.info("Task to be deleted was not found");
			return null;
		}
	}

	@PutMapping("/tasks/{id}")
	public ResponseEntity<TaskForm> updateTask(@PathVariable Long id, @RequestBody TaskForm patch) {
		Optional<Task> optional = taskRepository.findById(id);
		if (!optional.isPresent()) {
			throw new RequestException(HttpStatus.NOT_FOUND, "Task not found.");
		}
		Task task = optional.get();
		// Note that you cannot change the employee Id.
		// If this is required just delete and make new task
		if (patch.getTaskName() != task.getTaskName()) {
			task.setTaskName(patch.getTaskName());
		}
		if (patch.isCompleted() != task.isCompleted()) {
			task.setCompleted(task.isCompleted());
		}
		if (patch.getDescription() != null) {
			task.setDescription(patch.getDescription());
		}
		if (patch.getDueDate() != null) {
			task.setDueDate(patch.getDueDate());
		}
		if (patch.getStartDate() != null) {
			task.setStartDate(patch.getStartDate());
		}
		taskRepository.save(task);
		return new ResponseEntity<TaskForm>(patch, HttpStatus.OK);
	}

	@GetMapping("/tasks")
	public List<TaskForm> getTasksByAdmin() {
		Admin me = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Task> tasks = taskRepository.findByAdmin(me);
		List<TaskForm> taskForms = new ArrayList<>();
		for (Task task : tasks) {
			taskForms.add(TaskForm.fromTask(task));
		}
		return taskForms;
	}

	private List<Task> findTasksByEmployeeId(Long employeeId) {
		Optional<Employee> optional = employeeRepository.findById(employeeId);
		if (!optional.isPresent())
			throw new RequestException(HttpStatus.NOT_FOUND, "Employee not found.");
		Employee employee = optional.get();
		return taskRepository.findByEmployee(employee);
	}
}
