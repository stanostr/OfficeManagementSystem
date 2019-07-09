package com.stanostrovskii.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stanostrovskii.dao.MeetingRoomRepository;
import com.stanostrovskii.dao.TrainingRoomRepository;
import com.stanostrovskii.model.MeetingRoom;
import com.stanostrovskii.model.TrainingRoom;
import com.stanostrovskii.service.EmailService;

@RestController("/employee")
public class EmployeeRoomController {

	@Autowired
	private TrainingRoomRepository trainingRepository;
	
	@Autowired
	private MeetingRoomRepository meetingRepository;
	
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
	
	
}
