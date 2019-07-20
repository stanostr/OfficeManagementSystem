package com.stanostrovskii.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stanostrovskii.RequestException;
import com.stanostrovskii.dao.EmployeeRepository;
import com.stanostrovskii.dao.LeaveRepository;
import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.LeaveRequest;
import com.stanostrovskii.model.LeaveRequest.Status;
import com.stanostrovskii.service.EmailService;
import com.stanostrovskii.util.EmployeeEmailUtil;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/admin", produces = "application/json")
@Api(tags = { "Admin: Leave Management" })
public class AdminLeaveController {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private LeaveRepository leaveRepository;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/leave_requests")
	public List<LeaveRequest> viewAllRequests()
	{
		List<LeaveRequest> requests = new ArrayList<>();
		leaveRepository.findAll().forEach(requests::add);
		requests.forEach(r->r.getEmployee().setPassword(null));
		return requests;
	}
	
	@GetMapping("/leave_requests/{status}")
	public List<LeaveRequest> viewRequestsByStatus(@PathVariable String status)
	{
		Status s =  Status.valueOf(status);
		return leaveRepository.findByStatus(s);
	}
	
	
	@GetMapping("/leave_requests/employee")
	public List<LeaveRequest> viewRequestsByEmployee(@RequestParam Long id)
	{
		Optional<Employee> optional = employeeRepository.findById(id);
		if(!optional.isPresent())
			throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "No such employee exists");
		Employee employee = optional.get();
		List<LeaveRequest> requests = leaveRepository.findByEmployee(employee);
		requests.forEach(r->r.getEmployee().setPassword(null));
		return requests;
	}
	
	@PutMapping("/leave_requests/{id}")
	public void processRequest(@PathVariable Long id, @RequestParam String status)
	{
		try {
			LeaveRequest request = leaveRepository.findById(id).get();
			Status newStatus =  Status.valueOf(status);
			request.setStatus(newStatus);
			leaveRepository.save(request);
			if(newStatus.equals(Status.APPROVED))
				EmployeeEmailUtil.sendLeaveApprovedEmail(request, emailService);
			else if(newStatus.equals(Status.REJECTED))
				EmployeeEmailUtil.sendLeaveRejectionEmail(request, emailService);
		} catch (Exception e) {
			throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred!");
		}
	}
	
	@DeleteMapping("/leave_requests/{id}")
	public void deleteById(@PathVariable Long id)
	{
		try {
			leaveRepository.deleteById(id);
		} catch (Exception e) {
			throw new RequestException(HttpStatus.NOT_FOUND, "Leave request not found.");
		}
	}
}
