package com.stanostrovskii.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.stanostrovskii.dao.MeetingRoomRepository;
import com.stanostrovskii.dao.RoomReservationRepository;
import com.stanostrovskii.dao.TrainingRoomRepository;
import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.MeetingRoom;
import com.stanostrovskii.model.RoomReservation;
import com.stanostrovskii.model.TrainingRoom;
import com.stanostrovskii.service.EmailService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(path = "/employee", produces = "application/json")
@Api(tags = {"Employee"})
public class EmployeeRoomController {
	
	private static final Logger log = LoggerFactory.getLogger(EmployeeRoomController.class);

	@Autowired
	private TrainingRoomRepository trainingRepository;
	
	@Autowired
	private MeetingRoomRepository meetingRepository;
	
	@Autowired
	private RoomReservationRepository reservationRepository;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/training")
	public List<TrainingRoom> viewAllTrainingRooms()
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
	
	@PostMapping("/training/reserve")
	public ResponseEntity<RoomReservation> reserveTrainingRoom(@RequestBody RoomReservation reservation)
	{
		List<RoomReservation> reservations = reservationRepository.findByRoom(reservation.getRoom());
		
		log.info("We have " + reservations.size() + " reservations for this room");
		for(RoomReservation r: reservations)
		{
			if(!r.getStartTime().after(reservation.getEndTime()) && !r.getStartTime().before(reservation.getStartTime()) ||
					!r.getEndTime().after(reservation.getEndTime()) && !r.getEndTime().before(reservation.getStartTime())) 
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		if(reservation.getId()==null) {
			Employee me = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			reservation.setEmployee(me);
		}
		reservation = reservationRepository.save(reservation);
		reservation.setEmployee(null);
		return new ResponseEntity<RoomReservation>(reservation, HttpStatus.CREATED);
	}
	
	//TODO 
	@GetMapping("/training/reserve/{id}")
	public ResponseEntity<RoomReservation> getReservation(@PathVariable Long id)
	{
		RoomReservation reservation = reservationRepository.findById(id).get();
		return new ResponseEntity<RoomReservation>(reservation, HttpStatus.OK);
	}
}
