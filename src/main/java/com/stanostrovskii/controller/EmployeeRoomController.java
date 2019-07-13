package com.stanostrovskii.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stanostrovskii.RequestException;
import com.stanostrovskii.dao.MeetingRoomRepository;
import com.stanostrovskii.dao.RoomReservationRepository;
import com.stanostrovskii.dao.TrainingRoomRepository;
import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.rooms.EmployeeRoomReservationRequest;
import com.stanostrovskii.model.rooms.MeetingRoom;
import com.stanostrovskii.model.rooms.Room;
import com.stanostrovskii.model.rooms.RoomReservation;
import com.stanostrovskii.model.rooms.TrainingRoom;
import com.stanostrovskii.model.rooms.RoomReservation.Status;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/employee", produces = "application/json")
@Api(tags = { "Employee" })
public class EmployeeRoomController {

	@Autowired
	private TrainingRoomRepository trainingRepository;

	@Autowired
	private MeetingRoomRepository meetingRepository;

	@Autowired
	private RoomReservationRepository reservationRepository;

	@GetMapping("/training")
	public List<TrainingRoom> viewAllTrainingRooms() {
		List<TrainingRoom> list = new ArrayList<>();
		trainingRepository.findAll().forEach(list::add);
		return list;
	}

	@GetMapping("/meeting")
	public List<MeetingRoom> getAllMeetingRooms() {
		List<MeetingRoom> list = new ArrayList<>();
		meetingRepository.findAll().forEach(list::add);
		return list;
	}

	@PostMapping("/training/reserve")
	public ResponseEntity<EmployeeRoomReservationRequest> reserveTrainingRoom(
			@RequestBody EmployeeRoomReservationRequest request) {
		Room room = trainingRepository.findById(request.getRoomId()).get();
		return processReservationRequest(request, room);
	}

	@PostMapping("/meeting/reserve")
	public ResponseEntity<EmployeeRoomReservationRequest> reserveMeetingRoom(
			@RequestBody EmployeeRoomReservationRequest request) {
		Room room = meetingRepository.findById(request.getRoomId()).get();
		return processReservationRequest(request, room);
	}

	private ResponseEntity<EmployeeRoomReservationRequest> processReservationRequest(
			EmployeeRoomReservationRequest request, Room room) {
		// rejects immediately if the request interferes with another approved request
		List<RoomReservation> reservations = reservationRepository.findByRoomAndStatus(room, Status.APPROVED);
		for (RoomReservation r : reservations) {
			if (r.getStartTime().before(request.getEndTime()) && !r.getStartTime().before(request.getStartTime())
					|| !r.getEndTime().after(request.getEndTime()) && r.getEndTime().after(request.getStartTime())) {
				throw new RequestException(HttpStatus.BAD_REQUEST, "This time slot is unavailable!");
			}
		}
		RoomReservation reservation = new RoomReservation();
		Employee me = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		reservation.setEmployee(me);
		reservation.setStartTime(request.getStartTime());
		reservation.setEndTime(request.getEndTime());
		reservation.setRoom(room);
		reservationRepository.save(reservation);
		return new ResponseEntity<EmployeeRoomReservationRequest>(request, HttpStatus.CREATED);
	}

	@GetMapping("/reservations")
	public List<RoomReservation> getAllReservationsFromEmployee() {
		Employee me = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<RoomReservation> reservations = reservationRepository.findByEmployee(me);
		reservations.forEach(r -> r.setEmployee(null));
		return reservations;
	}

	@GetMapping("/reservations/{id}")
	public ResponseEntity<RoomReservation> getReservation(@PathVariable Long id) {
		RoomReservation reservation = reservationRepository.findById(id).get();
		reservation.setEmployee(null); // do not transmit employee info
		return new ResponseEntity<RoomReservation>(reservation, HttpStatus.OK);
	}
}
