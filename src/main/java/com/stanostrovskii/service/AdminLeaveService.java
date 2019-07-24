package com.stanostrovskii.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.stanostrovskii.RequestException;
import com.stanostrovskii.dao.EmployeeRepository;
import com.stanostrovskii.dao.LeaveRepository;
import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.LeaveRequest;
import com.stanostrovskii.model.LeaveRequest.Status;

@Service
public class AdminLeaveService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private LeaveRepository leaveRepository;

	public List<LeaveRequest> getAllRequests() {
		List<LeaveRequest> requests = new ArrayList<>();
		leaveRepository.findAll().forEach(requests::add);
		return requests;
	}

	public List<LeaveRequest> findByStatus(String status) {
		Status s = Status.valueOf(status);
		return leaveRepository.findByStatus(s);
	}

	public List<LeaveRequest> requestsByEmployee(Long id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		if (!optional.isPresent())
			throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "No such employee exists");
		Employee employee = optional.get();
		return leaveRepository.findByEmployee(employee);
	}

	public LeaveRequest findById(Long id) {
		try {
			return leaveRepository.findById(id).get();
		} catch (Exception e) {
			throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred!");
		}
	}

	public void saveRequest(LeaveRequest request) {
		leaveRepository.save(request);
	}

	public void deleteRequestById(Long id) {
		try {
			leaveRepository.deleteById(id);
		} catch (Exception e) {
			throw new RequestException(HttpStatus.NOT_FOUND, "Leave request not found.");
		}
	}
}
