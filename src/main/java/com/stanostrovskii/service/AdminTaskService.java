package com.stanostrovskii.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.stanostrovskii.RequestException;
import com.stanostrovskii.dao.TaskRepository;
import com.stanostrovskii.model.Admin;
import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.Task;
import com.stanostrovskii.model.TaskForm;

@Service
public class AdminTaskService {

	@Autowired
	private AdminEmployeeService userService;

	@Autowired
	private TaskRepository taskRepository;

	public List<Task> findTasksByEmployeeId(Long id) {
		Employee employee = userService.getEmployeeById(id);
		return taskRepository.findByEmployee(employee);
	}

	public void deleteTasksByEmployeeId(String id) {
		List<Task> tasks = findTasksByEmployeeId(Long.parseLong(id));
		taskRepository.deleteAll(tasks);		
	}

	public void updateTask(Long id, TaskForm patch) {
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
	}

	public Task saveTask(TaskForm taskForm, Employee employee, Admin me) {
		Task task = taskForm.toTask();
		task.setEmployee(employee);
		task.setAdmin(me);
		task = taskRepository.save(task);
		return task;
	}

	public TaskForm deleteTaskById(Long id) {
		try {
			TaskForm deletedForm = TaskForm.fromTask(taskRepository.findById(id).get());
			taskRepository.deleteById(id);
			return deletedForm;
		} catch (EmptyResultDataAccessException | NullPointerException e) {
			return null;
		}		
	}

	public List<Task> findByAdmin(Admin admin) {
		return taskRepository.findByAdmin(admin);
	}
}
