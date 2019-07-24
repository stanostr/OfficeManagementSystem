package com.stanostrovskii.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.LeaveRequest;
import com.stanostrovskii.model.LeaveRequest.Status;

public interface LeaveRepository extends CrudRepository<LeaveRequest, Long> {
	List<LeaveRequest> findByEmployee(Employee employee);
	List<LeaveRequest> findByStatus(Status status);
}
