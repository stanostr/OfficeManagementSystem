package com.stanostrovskii.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.stanostrovskii.model.rooms.MeetingRoom;
import com.stanostrovskii.model.rooms.RoomReservation;
import com.stanostrovskii.model.rooms.RoomReservation.Status;
import com.stanostrovskii.service.AdminRoomService;
import com.stanostrovskii.model.rooms.TrainingRoom;
import io.swagger.annotations.Api;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/admin", produces = "application/json")
@Api(tags = { "Admin: Room Management" })
public class AdminRoomController {
	
	@Autowired
	private AdminRoomService roomService;



	@GetMapping("/training")
	public List<TrainingRoom> getAllTrainingRooms() {
		return roomService.getAllTrainingRoom();
	}

	@GetMapping("/meeting")
	public List<MeetingRoom> getAllMeetingRooms() {
		return roomService.getAllMeetingRooms();
	}

	@GetMapping("/training/{id}")
	public ResponseEntity<TrainingRoom> getTrainingRoomById(@PathVariable Long id) {
		TrainingRoom room = roomService.getTrainingRoomById(id);
		return new ResponseEntity<TrainingRoom>(room, HttpStatus.OK);
	}

	@GetMapping("/meeting/{id}")
	public ResponseEntity<MeetingRoom> getMeetingRoomById(@PathVariable Long id) {
		MeetingRoom room = roomService.getMeetingRoomById(id);
		return new ResponseEntity<MeetingRoom>(room, HttpStatus.OK);
	}

	@PostMapping("/training")
	@ResponseStatus(HttpStatus.CREATED)
	public TrainingRoom addTrainingRoom(@RequestBody TrainingRoom trainingRoom) {
		return roomService.saveTrainingRoom(trainingRoom);
	}

	@PostMapping("/meeting")
	@ResponseStatus(HttpStatus.CREATED)
	public MeetingRoom addMeetingRoom(@RequestBody MeetingRoom meetingRoom) {
		return roomService.saveMeetingRoom(meetingRoom);
	}

	@PutMapping(value = "/training/{id}")
	public ResponseEntity<TrainingRoom> updateTrainingRoom(@PathVariable Long id, @RequestBody TrainingRoom patch) {
		TrainingRoom room = roomService.updateTrainingRoom(id, patch);
		return new ResponseEntity<>(room, HttpStatus.OK);
	}

	@PutMapping(value = "/meeting/{id}")
	public ResponseEntity<MeetingRoom> updateMeetingRoom(@PathVariable String id, @RequestBody MeetingRoom patch) {
		MeetingRoom room = roomService.updateMeetingRoom(id, patch);
		return new ResponseEntity<>(room, HttpStatus.OK);
	}

	@Transactional
	@DeleteMapping(value = "/meeting/{id}")
	public void deleteMeetingRoom(@PathVariable Long id) {
		roomService.deleteMeetingRoomById(id);
	}

	@Transactional
	@DeleteMapping(value = "/training/{id}")
	public void deleteTrainingRoom(@PathVariable Long id) {
		roomService.deleteTrainingRoomById(id);
	}

	@GetMapping("/reservations")
	public List<RoomReservation> allReservations() {
		return roomService.getAllReservations();
	}

	@GetMapping("/reservations/{status}")
	public List<RoomReservation> getReservationsByStatus(@PathVariable String status) {
		return roomService.getReservationsByStatus(Status.valueOf(status));
	}

	@PutMapping("/reservations/{id}")
	public void processReservation(@PathVariable Long id, @RequestParam String status) {
		roomService.processReservation(id, status);
	}

	@DeleteMapping("/reservations/rejected")
	public void deleteRejectedReservations() {
		roomService.deleteRejectedReservations();
	}

	@DeleteMapping("/reservations/{id}")
	public void deleteById(@PathVariable Long id) {
		roomService.deleteReservationById(id);
	}
}
