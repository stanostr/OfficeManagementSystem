package com.stanostrovskii.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.stanostrovskii.model.Room;
import com.stanostrovskii.model.RoomReservation;

public interface RoomReservationRepository extends CrudRepository<RoomReservation, Long> {
	List<RoomReservation> findByRoom(Room room);
}
