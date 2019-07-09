package com.stanostrovskii.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.stanostrovskii.model.Employee;
import com.stanostrovskii.model.rooms.Room;
import com.stanostrovskii.model.rooms.RoomReservation;

public interface RoomReservationRepository extends CrudRepository<RoomReservation, Long> {
	List<RoomReservation> findByRoom(Room room);
	List<RoomReservation> findByRoomAndStatus(Room room, RoomReservation.Status status);
	List<RoomReservation> findByEmployee(Employee employee);
}
