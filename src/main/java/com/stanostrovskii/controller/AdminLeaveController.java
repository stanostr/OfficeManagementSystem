package com.stanostrovskii.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stanostrovskii.model.LeaveRequest;
import com.stanostrovskii.model.LeaveRequest.Status;
import com.stanostrovskii.service.AdminLeaveService;
import com.stanostrovskii.service.EmailService;
import com.stanostrovskii.util.EmployeeEmailUtil;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/admin", produces = "application/json")
@Api(tags = { "Admin: Leave Management" })
public class AdminLeaveController {

	@Autowired
	private AdminLeaveService leaveService;

	@Autowired
	private EmailService emailService;

	@GetMapping("/leave_requests")
	public List<LeaveRequest> viewAllRequests() {
		List<LeaveRequest> requests = leaveService.getAllRequests();
		requests.forEach(r -> r.getEmployee().setPassword(null));
		return requests;
	}

	@GetMapping("/leave_requests/{status}")
	public List<LeaveRequest> viewRequestsByStatus(@PathVariable String status) {
		return leaveService.findByStatus(status);
	}

	@GetMapping("/leave_requests/employee")
	public List<LeaveRequest> viewRequestsByEmployee(@RequestParam Long id) {
		List<LeaveRequest> requests = leaveService.requestsByEmployee(id);
		requests.forEach(r -> r.getEmployee().setPassword(null));
		return requests;
	}

	@PutMapping("/leave_requests/{id}")
	public void processRequest(@PathVariable Long id, @RequestParam String status) {

		LeaveRequest request = leaveService.findById(id);
		Status newStatus = Status.valueOf(status);
		request.setStatus(newStatus);
		leaveService.saveRequest(request);
		if (newStatus.equals(Status.APPROVED))
			EmployeeEmailUtil.sendLeaveApprovedEmail(request, emailService);
		else if (newStatus.equals(Status.REJECTED))
			EmployeeEmailUtil.sendLeaveRejectionEmail(request, emailService);

	}

	@DeleteMapping("/leave_requests/{id}")
	public void deleteById(@PathVariable Long id) {
		leaveService.deleteRequestById(id);
	}
}
