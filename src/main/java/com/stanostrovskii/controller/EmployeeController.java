package com.stanostrovskii.controller;

import java.util.ArrayList;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.LeaveRequest;
import com.stanostrovskii.model.rooms.EmployeeRoomReservationRequest;
import com.stanostrovskii.model.rooms.MeetingRoom;
import com.stanostrovskii.model.rooms.RoomReservation;
import com.stanostrovskii.model.rooms.TrainingRoom;
import com.stanostrovskii.model.Task;
import com.stanostrovskii.model.TaskForm;
import com.stanostrovskii.service.EmailService;
import com.stanostrovskii.service.EmployeeActionsService;
import com.stanostrovskii.util.EmployeeEmailUtil;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path="/employee", produces = "application/json")
@Api(tags = { "Employee" })
public class EmployeeController {
	@Autowired
	private EmployeeActionsService employeeService;

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
		List<Task> tasks = employeeService.getTasksByEmployee(thisEmployee);
		List<TaskForm> taskForms = new ArrayList<>();
		for (Task task : tasks) {
			taskForms.add(TaskForm.fromTask(task));
		}
		return taskForms;
	}

	@PutMapping("/tasks/{id}")
	public void completeTask(@PathVariable Long id) {
		Task completedTask = employeeService.completeTask(id);
		if (completedTask!=null)
			EmployeeEmailUtil.sendCompletedTaskEmail(completedTask, emailService);
	}
	
	@GetMapping("/leave_requests")
	public List<LeaveRequest> viewMyRequests()
	{
		Employee thisEmployee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return employeeService.leaveRequestsByEmployee(thisEmployee);
	}
	
	@GetMapping("/leave_requests/{id}")
	public LeaveRequest viewRequestById(@PathVariable Long id)
	{
		return employeeService.leaveRequestById(id);
	}
	
	@DeleteMapping("/leave_requests/{id}")
	public LeaveRequest deleteLeaveRequest(@PathVariable Long id)
	{
		return employeeService.deleteRequestById(id);
	}
	
	@PostMapping("/leave_requests")
	public LeaveRequest postLeaveRequest(@RequestBody LeaveRequest request)
	{
		if(request.getEmployee()==null)
		{
			Employee thisEmployee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			request.setEmployee(thisEmployee);
		}
		return employeeService.addLeaveRequest(request);
	}
	
	//
	@GetMapping("/training")
	public List<TrainingRoom> viewAllTrainingRooms() {
		return employeeService.getAllTrainingRooms();
	}

	@GetMapping("/meeting")
	public List<MeetingRoom> getAllMeetingRooms() {
		return employeeService.getAllMeetingRooms();
	}

	@PostMapping("/training/reserve")
	public ResponseEntity<EmployeeRoomReservationRequest> reserveTrainingRoom(
			@RequestBody EmployeeRoomReservationRequest request) {
		Employee me = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return employeeService.processTrainingRoomReservation(request, me);
	
	}

	@PostMapping("/meeting/reserve")
	public ResponseEntity<EmployeeRoomReservationRequest> reserveMeetingRoom(
			@RequestBody EmployeeRoomReservationRequest request) {
		Employee me = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return employeeService.processMeetingRoomReservation(request, me);
	}

	@GetMapping("/reservations")
	public List<RoomReservation> getAllReservationsFromEmployee() {
		Employee me = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return employeeService.findAllReservationsByEmployee(me);
	}

	

	@GetMapping("/reservations/{id}")
	public ResponseEntity<RoomReservation> getReservation(@PathVariable Long id) {
		return employeeService.getReservationById(id);
	}


}
