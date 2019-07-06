package com.stanostrovskii.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.Task;

public interface TaskRepository extends CrudRepository<Task, Long>
{
	List<Task> findByEmployee(Employee employee);
	List<Task> findByEmployeeAndCompletedTrue(Employee employee);
	List<Task> findByEmployeeAndCompletedFalse(Employee employee);
}
