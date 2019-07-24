package com.stanostrovskii.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.stanostrovskii.RequestException;
import com.stanostrovskii.dao.LeaveRepository;
import com.stanostrovskii.dao.MeetingRoomRepository;
import com.stanostrovskii.dao.RoomReservationRepository;
import com.stanostrovskii.dao.TaskRepository;
import com.stanostrovskii.dao.TrainingRoomRepository;
import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.LeaveRequest;
import com.stanostrovskii.model.Task;
import com.stanostrovskii.model.LeaveRequest.Status;
import com.stanostrovskii.model.rooms.EmployeeRoomReservationRequest;
import com.stanostrovskii.model.rooms.MeetingRoom;
import com.stanostrovskii.model.rooms.Room;
import com.stanostrovskii.model.rooms.RoomReservation;
import com.stanostrovskii.model.rooms.TrainingRoom;

@Service
public class EmployeeActionsService {
	@Autowired
	private LeaveRepository leaveRepository;
	
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TrainingRoomRepository trainingRepository;
	
	@Autowired
	private MeetingRoomRepository meetingRepository;
	
	@Autowired
	private RoomReservationRepository reservationRepository;
	
	public List<Task> getTasksByEmployee(Employee employee)
	{
		return taskRepository.findByEmployee(employee);
	}
	
	public Task completeTask(Long id)
	{
		Optional<Task> optional = taskRepository.findById(id);
		if (!optional.isPresent())
			return null;
		Task task = optional.get();
		if(task.isCompleted()) return null;
		task.setCompleted(true);
		return taskRepository.save(task);
	}

	public List<LeaveRequest> leaveRequestsByEmployee(Employee thisEmployee) {
		return leaveRepository.findByEmployee(thisEmployee);
	}

	public LeaveRequest leaveRequestById(Long id) {
		Optional<LeaveRequest> optional = leaveRepository.findById(id);
		if (!optional.isPresent())
			throw new RequestException(HttpStatus.NOT_FOUND, "Leave request not found.");
		return optional.get();
	}

	public LeaveRequest addLeaveRequest(LeaveRequest request) {
		request.setStatus(Status.PENDING); //employees can only make pending leave requests
		request = leaveRepository.save(request);
		request.getEmployee().setPassword(null);
		return request;
	}
	
	public LeaveRequest deleteRequestById(Long id) {
		LeaveRequest request = leaveRepository.findById(id).get();
		if(request==null) throw new RequestException(HttpStatus.NOT_FOUND, "Request not found!");
		leaveRepository.delete(request);
		return request;
	}

	public List<TrainingRoom> getAllTrainingRooms() {
		List<TrainingRoom> list = new ArrayList<>();
		trainingRepository.findAll().forEach(list::add);
		return list;
	}

	public List<MeetingRoom> getAllMeetingRooms() {
		List<MeetingRoom> list = new ArrayList<>();
		meetingRepository.findAll().forEach(list::add);
		return list;
	}
	
	
	private ResponseEntity<EmployeeRoomReservationRequest> processReservationRequest(
			EmployeeRoomReservationRequest request, Room room, Employee employee) {
		// rejects immediately if the request interferes with another approved request
		List<RoomReservation> reservations = reservationRepository.findByRoomAndStatus(room, RoomReservation.Status.APPROVED);
		for (RoomReservation r : reservations) {
			if (r.getStartTime().before(request.getEndTime()) && !r.getStartTime().before(request.getStartTime())
					|| !r.getEndTime().after(request.getEndTime()) && r.getEndTime().after(request.getStartTime())) {
				throw new RequestException(HttpStatus.BAD_REQUEST, "This time slot is unavailable!");
			}
		}
		RoomReservation reservation = new RoomReservation();
		reservation.setEmployee(employee);
		reservation.setStartTime(request.getStartTime());
		reservation.setEndTime(request.getEndTime());
		reservation.setRoom(room);
		reservationRepository.save(reservation);
		return new ResponseEntity<EmployeeRoomReservationRequest>(request, HttpStatus.CREATED);
	}

	public ResponseEntity<EmployeeRoomReservationRequest> processTrainingRoomReservation(
			EmployeeRoomReservationRequest request, Employee employee) {
		Room room = trainingRepository.findById(request.getRoomId()).get();
		return processReservationRequest(request, room, employee);
	}

	public ResponseEntity<EmployeeRoomReservationRequest> processMeetingRoomReservation(
			EmployeeRoomReservationRequest request, Employee employee) {
		Room room = meetingRepository.findById(request.getRoomId()).get();
		return processReservationRequest(request, room, employee);
	}
	
	public ResponseEntity<RoomReservation> getReservationById(Long id) {
		RoomReservation reservation = reservationRepository.findById(id).get();
		reservation.setEmployee(null); // do not transmit employee info
		return new ResponseEntity<RoomReservation>(reservation, HttpStatus.OK);
	}
	
	public List<RoomReservation> findAllReservationsByEmployee(Employee employee) {
		List<RoomReservation> reservations = reservationRepository.findByEmployee(employee);
		reservations.forEach(r -> r.setEmployee(null));
		return reservations;
	}
}
