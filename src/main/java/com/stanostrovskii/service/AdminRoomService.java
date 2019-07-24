package com.stanostrovskii.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.stanostrovskii.RequestException;
import com.stanostrovskii.dao.MeetingRoomRepository;
import com.stanostrovskii.dao.RoomReservationRepository;
import com.stanostrovskii.dao.TrainingRoomRepository;
import com.stanostrovskii.model.rooms.MeetingRoom;
import com.stanostrovskii.model.rooms.RoomReservation;
import com.stanostrovskii.model.rooms.RoomReservation.Status;
import com.stanostrovskii.util.EmployeeEmailUtil;
import com.stanostrovskii.model.rooms.TrainingRoom;

@Service
public class AdminRoomService {

	@Autowired
	private TrainingRoomRepository trainingRepository;

	@Autowired
	private MeetingRoomRepository meetingRepository;

	@Autowired
	private RoomReservationRepository reservationRepository;

	@Autowired
	private EmailService emailService;

	public List<TrainingRoom> getAllTrainingRoom() {
		List<TrainingRoom> list = new ArrayList<>();
		trainingRepository.findAll().forEach(list::add);
		return list;
	}

	public List<MeetingRoom> getAllMeetingRooms() {
		List<MeetingRoom> list = new ArrayList<>();
		meetingRepository.findAll().forEach(list::add);
		return list;
	}

	public TrainingRoom getTrainingRoomById(Long id) {
		Optional<TrainingRoom> optRoom = trainingRepository.findById(id);
		if (!optRoom.isPresent())
			throw new RequestException(HttpStatus.NOT_FOUND, "Room not found.");
		return optRoom.get();
	}

	public MeetingRoom getMeetingRoomById(Long id) {
		Optional<MeetingRoom> optRoom = meetingRepository.findById(id);
		if (!optRoom.isPresent())
			throw new RequestException(HttpStatus.NOT_FOUND, "Room not found.");
		return optRoom.get();
	}

	public TrainingRoom saveTrainingRoom(TrainingRoom trainingRoom) {
		return trainingRepository.save(trainingRoom);
	}

	public MeetingRoom saveMeetingRoom(MeetingRoom meetingRoom) {
		return meetingRepository.save(meetingRoom);
	}

	public TrainingRoom updateTrainingRoom(Long id, TrainingRoom patch) {
		Optional<TrainingRoom> optRoom = trainingRepository.findById(id);
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
			return room;
		}
		throw new RequestException(HttpStatus.NOT_FOUND, "Room not found.");
	}

	public MeetingRoom updateMeetingRoom(String id, MeetingRoom patch) {
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
			return room;
		}
		throw new RequestException(HttpStatus.NOT_FOUND, "Room not found.");
	}

	public void deleteMeetingRoomById(Long id) {
		MeetingRoom room = meetingRepository.findById(id).get();
		if (room != null) {
			reservationRepository.deleteByRoom(room);
		}
		meetingRepository.deleteById(id);
	}

	public void deleteTrainingRoomById(Long id) {

		TrainingRoom room = trainingRepository.findById(id).get();
		if (room != null) {
			reservationRepository.deleteByRoom(room);
		}
		trainingRepository.deleteById(id);
	}

	public List<RoomReservation> getAllReservations() {
		List<RoomReservation> reservations = new ArrayList<RoomReservation>();
		reservationRepository.findAll().forEach(reservations::add);
		reservations.forEach(r -> r.getEmployee().setPassword(null));
		return reservations;
	}

	public List<RoomReservation> getReservationsByStatus(Status status) {
		List<RoomReservation> reservations = reservationRepository.findByStatus(status);
		reservations.forEach(r -> r.getEmployee().setPassword(null));
		return reservations;
	}

	public void processReservation(Long id, String status) {
		try {
			RoomReservation reservation = reservationRepository.findById(id).get();
			Status newStatus = Status.valueOf(status);
			reservation.setStatus(newStatus);
			reservationRepository.save(reservation);
			if (newStatus.equals(Status.APPROVED))
				EmployeeEmailUtil.sendReservationApprovedEmail(reservation, emailService);
			else if (newStatus.equals(Status.REJECTED))
				EmployeeEmailUtil.sendReservationRejectionEmail(reservation, emailService);
		} catch (Exception e) {
			throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred!");
		}
	}

	public void deleteRejectedReservations() {
		List<RoomReservation> rejects = reservationRepository.findByStatus(Status.REJECTED);
		reservationRepository.deleteAll(rejects);
	}

	public void deleteReservationById(Long id) {
		try {
			reservationRepository.deleteById(id);
		} catch (Exception e) {
			throw new RequestException(HttpStatus.NOT_FOUND, "Room not found.");
		}		
	}
}
