package com.stanostrovskii.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.stanostrovskii.model.rooms.TrainingRoom;

public interface TrainingRoomRepository extends CrudRepository<TrainingRoom, Long>{
	List<TrainingRoom> findByCapacityGreaterThan(int capacity);
	List<TrainingRoom> findByWhiteboardAvailTrue();
	List<TrainingRoom> findByProjectorAvailTrue();
}
