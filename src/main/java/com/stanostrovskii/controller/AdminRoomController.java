package com.stanostrovskii.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.stanostrovskii.RequestException;
import com.stanostrovskii.dao.MeetingRoomRepository;
import com.stanostrovskii.dao.RoomReservationRepository;
import com.stanostrovskii.dao.TrainingRoomRepository;
import com.stanostrovskii.model.rooms.MeetingRoom;
import com.stanostrovskii.model.rooms.RoomReservation;
import com.stanostrovskii.model.rooms.RoomReservation.Status;
import com.stanostrovskii.service.EmailService;
import com.stanostrovskii.model.rooms.TrainingRoom;
import com.stanostrovskii.util.EmployeeEmailUtil;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(path = "/admin", produces = "application/json")
@Api(tags = { "Admin: Room Management" })
public class AdminRoomController {
	
	@Autowired
	private TrainingRoomRepository trainingRepository;
	
	@Autowired
	private MeetingRoomRepository meetingRepository;
	
	@Autowired
	private RoomReservationRepository reservationRepository;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/training")
	public List<TrainingRoom> getAllTrainingRooms()
	{
		List<TrainingRoom> list = new ArrayList<>();
		trainingRepository.findAll().forEach(list::add);
		return list;
	}

	@GetMapping("/meeting")
	public List<MeetingRoom> getAllMeetingRooms()
	{
		List<MeetingRoom> list = new ArrayList<>();
		meetingRepository.findAll().forEach(list::add);
		return list;
	}
	
	@GetMapping("/training/{id}")
	public ResponseEntity<TrainingRoom> getTrainingRoomById(@PathVariable Long id)
	{
		Optional<TrainingRoom> optRoom = trainingRepository.findById(id);
		if(!optRoom.isPresent()) throw new RequestException(HttpStatus.NOT_FOUND, "Room not found.");
		return new ResponseEntity<TrainingRoom>(optRoom.get(), HttpStatus.OK);
	}
	
	@GetMapping("/meeting/{id}")
	public ResponseEntity<MeetingRoom> getMeetingRoomById(@PathVariable Long id)
	{
		Optional<MeetingRoom> optRoom = meetingRepository.findById(id);
		if(!optRoom.isPresent()) throw new RequestException(HttpStatus.NOT_FOUND, "Room not found.");
		return new ResponseEntity<MeetingRoom>(optRoom.get(), HttpStatus.OK);
	}
	
	@PostMapping("/training")
	@ResponseStatus(HttpStatus.CREATED)
	public TrainingRoom addTrainingRoom(@RequestBody TrainingRoom trainingRoom)
	{
		return trainingRepository.save(trainingRoom);
	}
	
	@PostMapping("/meeting")
	@ResponseStatus(HttpStatus.CREATED)
	public MeetingRoom addMeetingRoom(@RequestBody MeetingRoom meetingRoom)
	{
		return meetingRepository.save(meetingRoom);
	}
	
	@PutMapping(value = "/training/{id}")
	public ResponseEntity<TrainingRoom> updateTrainingRoom(@PathVariable String id, @RequestBody TrainingRoom patch) {
		Optional<TrainingRoom> optRoom = trainingRepository.findById(Long.parseLong(id));
		if (optRoom.isPresent()) {
			TrainingRoom room = optRoom.get();
			if (patch.getName() != null) {
				room.setName(room.getName());
			}
			if (patch.getCapacity() > 0) {
				room.setCapacity(patch.getCapacity());
			}
			room.setProjectorAvail(room.isProjectorAvail());
			room.setWhiteboardAvail(room.isWhiteboardAvail());
			room = trainingRepository.save(room);
			return new ResponseEntity<>(room, HttpStatus.OK);
		}
		throw new RequestException(HttpStatus.NOT_FOUND, "Room not found.");
	}
	
	@PutMapping(value = "/meeting/{id}")
	public ResponseEntity<MeetingRoom> updateMeetingRoom(@PathVariable String id, @RequestBody MeetingRoom patch) {
		Optional<MeetingRoom> optRoom = meetingRepository.findById(Long.parseLong(id));
		if (optRoom.isPresent()) {
			MeetingRoom room = optRoom.get();
			if (patch.getName() != null) {
				room.setName(room.getName());
			}
			if (patch.getCapacity() > 0) {
				room.setCapacity(patch.getCapacity());
			}
			room = meetingRepository.save(room);
			return new ResponseEntity<>(room, HttpStatus.OK);
		}
		throw new RequestException(HttpStatus.NOT_FOUND, "Room not found.");
	}
	
	@DeleteMapping(value = "/meeting/{id}")
	public void deleteMeetingRoom(@PathVariable String id)
	{
		meetingRepository.deleteById(Long.parseLong(id));
	}
	
	@DeleteMapping(value = "/training/{id}")
	public void deleteTrainingRoom(@PathVariable String id)
	{
		trainingRepository.deleteById(Long.parseLong(id));
	}
	
	@GetMapping("/reservations")
	public List<RoomReservation> allReservations()
	{
		List<RoomReservation> reservations = new ArrayList<RoomReservation>();
		reservationRepository.findAll().forEach(reservations::add);
		reservations.forEach(r->r.getEmployee().setPassword(null));
		return reservations;
	}
	
	@GetMapping("/reservations/{status}")
	public List<RoomReservation> getReservationsByStatus(@PathVariable String status)
	{
		List<RoomReservation> reservations = reservationRepository.findByStatus(Status.valueOf(status));
		reservations.forEach(r->r.getEmployee().setPassword(null));
		return reservations;
	}
	
	@PutMapping("/reservations/{id}")
	public void processReservation(@PathVariable Long id, @RequestParam String status)
	{
		try {
			RoomReservation reservation = reservationRepository.findById(id).get();
			Status newStatus =  Status.valueOf(status);
			reservation.setStatus(newStatus);
			reservationRepository.save(reservation);
			if(newStatus.equals(Status.APPROVED))
				EmployeeEmailUtil.sendReservationApprovedEmail(reservation, emailService);
			else if(newStatus.equals(Status.REJECTED))
				EmployeeEmailUtil.sendReservationRejectionEmail(reservation, emailService);
		} catch (Exception e) {
			throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred!");
		}
	}
	
	@DeleteMapping("/reservations/rejected")
	public void deleteRejectedReservations()
	{
		List<RoomReservation> rejects = reservationRepository.findByStatus(Status.REJECTED);
		reservationRepository.deleteAll(rejects);
	}

	@DeleteMapping("/reservations/{id}")
	public void deleteById(@PathVariable Long id)
	{
		try {
			reservationRepository.deleteById(id);
		} catch (Exception e) {
			throw new RequestException(HttpStatus.NOT_FOUND, "Room not found.");
		}
	}
}
