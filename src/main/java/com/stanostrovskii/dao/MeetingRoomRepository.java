package com.stanostrovskii.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.stanostrovskii.model.MeetingRoom;

public interface MeetingRoomRepository extends CrudRepository<MeetingRoom, Long>{
	List<MeetingRoom> findByCapacityGreaterThan(int capacity);
}
